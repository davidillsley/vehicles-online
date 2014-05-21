package views.disposal_of_vehicle

import pages.disposal_of_vehicle.MicroServiceErrorPage.{tryAgain, exit}
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.disposal_of_vehicle._

final class MicroserviceErrorIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" in new WebBrowser {
      go to MicroServiceErrorPage

      assert(page.title equals MicroServiceErrorPage.title)
    }
  }

  "tryAgain button" should {
    "redirect to vehiclelookup" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to MicroServiceErrorPage

      click on tryAgain

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when no details are cached" in new WebBrowser {
      go to MicroServiceErrorPage

      click on tryAgain

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }

  "exit button" should {
    "redirect to beforeyoustart" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to MicroServiceErrorPage

      click on exit

      assert(page.title equals BeforeYouStartPage.title)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      setupTradeDetailsIntegration().
      dealerDetailsIntegration()
}
