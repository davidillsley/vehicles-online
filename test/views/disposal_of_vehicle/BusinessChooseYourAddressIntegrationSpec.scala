package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.tags.UiTag
import helpers.webbrowser.TestHarness
import org.openqa.selenium.{By, WebElement, WebDriver}
import pages.common.ErrorPanel
import pages.disposal_of_vehicle.BusinessChooseYourAddressPage.{sadPath, happyPath, manualAddress, back}
import pages.disposal_of_vehicle._
import services.fakes.FakeAddressLookupService.PostcodeValid
import mappings.disposal_of_vehicle.EnterAddressManually._
import services.fakes.FakeAddressLookupService
import pages.common.AlternateLanguages._

final class BusinessChooseYourAddressIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage
      page.title should equal(BusinessChooseYourAddressPage.title)
    }

    "display the progress of the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage

      page.source.contains("Step 3 of 6") should equal(true)
    }

    "redirect when no traderBusinessName is cached" taggedAs UiTag in new WebBrowser {
      go to BusinessChooseYourAddressPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "not display 'No addresses found' message when address service returns addresses" taggedAs UiTag in new WebBrowser {
      SetupTradeDetailsPage.happyPath()
      page.source.contains("No addresses found for that postcode") should equal(false) // Does not contain message
    }

    "should display the postcode entered in the previous page" taggedAs UiTag in new WebBrowser {
      SetupTradeDetailsPage.happyPath()
      page.source.contains(FakeAddressLookupService.PostcodeValid.toUpperCase) should equal(true)
    }

    "display expected addresses in dropdown when address service returns addresses" taggedAs UiTag in new WebBrowser {
      SetupTradeDetailsPage.happyPath()

      BusinessChooseYourAddressPage.getListCount should equal(4) // The first option is the "Please select..." and the other options are the addresses.
      page.source should include(s"presentationProperty stub, 123, property stub, street stub, town stub, area stub, $PostcodeValid")
      page.source should include(s"presentationProperty stub, 456, property stub, street stub, town stub, area stub, $PostcodeValid")
      page.source should include(s"presentationProperty stub, 789, property stub, street stub, town stub, area stub, $PostcodeValid")
    }

    "display 'No addresses found' message when address service returns no addresses" taggedAs UiTag in new WebBrowser {
      SetupTradeDetailsPage.submitInvalidPostcode

      page.source should include("No addresses found for that postcode") // Does not contain the positive message
    }

    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      SetupTradeDetailsPage.happyPath()
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CsrfPreventionAction.tokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CsrfPreventionAction.tokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }
  }

  "manualAddress button" should {
    "go to the manual address entry page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage

      click on manualAddress

      page.title should equal(EnterAddressManuallyPage.title)
    }
  }

  "back button" should {
    "display previous page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to BusinessChooseYourAddressPage

      click on back

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }

  "select button" should {
    "go to the next page when correct data is entered" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      happyPath

      page.title should equal(VehicleLookupPage.title)
    }

    "display validation error messages when addressSelected is not in the list" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      sadPath

      ErrorPanel.numberOfErrors should equal(1)
    }

    "remove redundant EnterAddressManually cookie (as we are now in an alternate history)" taggedAs UiTag in new WebBrowser {
      def cacheSetupVisitedEnterAddressManuallyPage()(implicit webDriver: WebDriver) =
        CookieFactoryForUISpecs.setupTradeDetails().
          enterAddressManually()

      go to BeforeYouStartPage
      cacheSetupVisitedEnterAddressManuallyPage()
      happyPath

      // Verify the cookies identified by the full set of cache keys have been removed
      webDriver.manage().getCookieNamed(EnterAddressManuallyCacheKey) should equal(null)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.setupTradeDetails()
}
