package controllers.change_of_address

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class AuthenticationIntegrationSpec extends Specification with Tags {

  val PINValid = "123456"

  "Authentication Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/authentication")

      // Assert
      titleMustContain("authentication")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/authentication")

      // Act
      browser.fill("#PIN") `with` PINValid
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - retrieve a vehicle record")
    }
  }
}

