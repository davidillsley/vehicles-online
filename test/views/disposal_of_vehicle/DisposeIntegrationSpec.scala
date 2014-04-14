package views.disposal_of_vehicle

import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec
import services.fakes.FakeDateServiceImpl._

class DisposeIntegrationSpec extends UiSpec with TestHarness {
  private def cacheSetup() = {
    CacheSetup.businessChooseYourAddress().
      vehicleDetailsModel()
  }

  "Dispose Integration" should {
    "be presented" in new WebBrowser {
      cacheSetup()
      go to DisposePage.url
      assert(page.title equals DisposePage.title)
    }

    "display DisposeSuccess page on correct submission" in new WebBrowser {
      cacheSetup().
        vehicleLookupFormModel()
      DisposePage.happyPath
      assert(page.title equals DisposeSuccessPage.title)
    }

    "display validation errors when no data is entered" in new WebBrowser {
      cacheSetup()
      DisposePage.sadPath
      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "redirect when no vehicleDetailsModel is cached" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      go to DisposePage.url
      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect when no businessChooseYourAddress is cached" in new WebBrowser {
      CacheSetup.vehicleDetailsModel()
      go to DisposePage.url
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      go to DisposePage.url
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display validation errors when month and year are input but no day" in new WebBrowser {
      cacheSetup()
      go to DisposePage.url
      DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
      DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
      click on DisposePage.consent
      click on DisposePage.lossOfRegistrationConsent
      click on DisposePage.dispose
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation errors when day and year are input but no month" in new WebBrowser {
      cacheSetup()
      go to DisposePage.url
      DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
      DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
      click on DisposePage.consent
      click on DisposePage.lossOfRegistrationConsent
      click on DisposePage.dispose
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation errors when day and month are input but no year" in new WebBrowser {
      cacheSetup()
      go to DisposePage.url
      DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
      DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
      click on DisposePage.consent
      click on DisposePage.lossOfRegistrationConsent
      click on DisposePage.dispose
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display previous page when back link is clicked" in new WebBrowser {
      cacheSetup()
      go to DisposePage.url
      click on DisposePage.back
      assert(page.title equals VehicleLookupPage.title)
    }
  }
}
