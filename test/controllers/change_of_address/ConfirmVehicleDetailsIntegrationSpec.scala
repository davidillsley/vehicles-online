
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class ConfirmVehicleDetailsIntegrationSpec extends Specification with Tags {

  "ConfirmVehicleDetails Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      // Pass credentials through login page
      Formulate.loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      // Complete V5c search page with vehicle details
      Formulate.v5cSearchPagePopulate(browser)

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details")
    }


    "redirect to login when login cache is empty" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/confirm-vehicle-details")

      // Assert
      titleMustContain("Change of keeper - are you registered")
    }


    "v5c search page is presented when user is logged in but not entered vehicle details" in new WithBrowser with BrowserMatchers {
      //Arrange
      // Pass credentials through login page
      Formulate.loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      //Act
      //Try to access confirm vehicle details page without entering V5c details
      browser.goTo("/confirm-vehicle-details")

      // Assert
      titleMustEqual("Change of keeper - retrieve a vehicle record")
    }


    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      // Pass credentials through login page and click submit
      Formulate.loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      // Complete V5c search page
      Formulate.v5cSearchPagePopulate(browser)

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details") //TODO: Need to point at next page once it is built
    }
  }
}