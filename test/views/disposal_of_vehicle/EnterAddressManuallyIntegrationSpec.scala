package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec
import services.fakes.FakeAddressLookupService._
import EnterAddressManuallyPage._
import services.session.{PlaySessionState, SessionState}
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import org.openqa.selenium.WebDriver

class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {

  "EnterAddressManually integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      go to EnterAddressManuallyPage

      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "accept and redirect when all fields are input with valid entry" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath()

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPathMandatoryFieldsOnly()

      assert(page.title equals VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      sadPath

      assert(ErrorPanel.numberOfErrors equals 5)
    }

    "display validation error messages when a blank line1 is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = "")

      assert(ErrorPanel.numberOfErrors equals 2)
    }


    "display validation error messages when line1 is entered which is greater than max length" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = "a" * 76)

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a blank postcode is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display validation error messages when a postcode is entered containing special characters" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "SA99 1D!")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered containing letters only" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "SQWER")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered containing numbers only" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "12345")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered in an incorrect format" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "SA99 1B1")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered less than min length" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(postcode = "SA")

      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validaton error message when an invalid character is entered into address line 1 !" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 %" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 +" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 £" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 ^" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 (" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "(")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line1 contains ," in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line1 contains ." in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line1 contains /" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "/")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line1 contains \\" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line1 = line1Valid + "\\")

      assert(page.title equals VehicleLookupPage.title)
    }


    "display one validaton error message when an invalid character is entered into address line 2 !" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 %" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 #" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "#")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 +" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 £" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 ^" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 (" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "(")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line2 contains ," in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line2 contains ." in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line2 contains /" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "/")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line2 contains \\" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line2 = line2Valid + "\\")

      assert(page.title equals VehicleLookupPage.title)
    }

    "display one validaton error message when an invalid character is entered into address line 3 !" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 %" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 #" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "#")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 +" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 £" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 ^" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 (" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "(")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line3 contains ," in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line3 contains ." in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line3 contains /" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "/")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line3 contains \\" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line3 = line3Valid + "\\")

      assert(page.title equals VehicleLookupPage.title)
    }

    "display one validaton error message when an invalid character is entered into address line 4 !" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }


    "display one validaton error message when an invalid character is entered into address line 4 %" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 #" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "#")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 +" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 £" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 ^" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 (" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "(")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line4 contains ," in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line4 contains ." in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line4 contains /" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "/")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line4 contains \\" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      happyPath(line4 = line4Valid + "\\")

      assert(page.title equals VehicleLookupPage.title)
    }
  }

  private def cacheSetup(sessionState: SessionState)(implicit webDriver: WebDriver) =
    new CacheSetup(sessionState).setupTradeDetailsIntegration()

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
