package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.BeforeYouStartPage.startNow
import pages.disposal_of_vehicle._

class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness  {

  "BeforeYouStart Integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage

      assert(page.title equals BeforeYouStartPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      go to BeforeYouStartPage

      click on startNow

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}