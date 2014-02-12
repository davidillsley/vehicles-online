package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPopulate, SetUpTradeDetailsPopulate}

class BusinessChooseYourAddressIntegrationSpec extends Specification with Tags {
  "business_choose_your_address Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPopulate.happyPath(browser)
      browser.goTo(BusinessChooseYourAddressPopulate.url)

      // Assert
      titleMustEqual("Business: Choose your address")
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPopulate.happyPath(browser)
      BusinessChooseYourAddressPopulate.happyPath(browser)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade: vehicle")
    }

    "redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(BusinessChooseYourAddressPopulate.url)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade: set-up")
    }

    "display one validation error messages when addressSelected is not in the list" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPopulate.happyPath(browser)
      BusinessChooseYourAddressPopulate.sadPath(browser)

      //Assert
      checkNumberOfValidationErrors(1)
    }
  }
}