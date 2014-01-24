import controllers.change_of_address.Authentication
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class AuthenticationFormSpec extends Specification with Tags {
  "Authentication Form" should {
    //title values
    val PINFormID = "PIN"
    val PINValid = "123456"

    def authenticationFiller(PIN: String) = {
      Authentication.authenticationForm.bind(
        Map(
          PINFormID -> PIN
        )
      )
    }

    "reject when PIN is empty" in {
      authenticationFiller(PIN="").fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          //errors for min length, regex, required
        },
        f => "An error should occur" must equalTo("Valid")
      )
    }

    "reject when PIN contains letters" in {
      authenticationFiller(PIN="abcdef").fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          //error for regex
        },
        f => "An error should occur" must equalTo("Valid")
      )
    }

    "reject when PIN contains special characters" in {
      authenticationFiller(PIN="£").fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
          //errors for regex and min length
        },
        f => "An error should occur" must equalTo("Valid")
      )
    }

    "reject when PIN is less than min length" in {
      authenticationFiller(PIN="12345").fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          //error for min length
        },
        f => "An error should occur" must equalTo("Valid")
      )
    }

    "reject when PIN is more than max length" in {
      authenticationFiller(PIN="1234567").fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          //error for max length
        },
        f => "An error should occur" must equalTo("Valid")
      )
    }
/* TODO Get acceptance test working currently parameter error
    "accept when PIN is valid" in {
      authenticationFiller(PIN=PINValid).fold(
        formWithErrors => {failure("An error should occur")
        },
        f => f.PIN must equalTo(PINValid)
      )
    }
*/
  }
}