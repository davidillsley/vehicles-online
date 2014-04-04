package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.SetupTradeDetails._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import helpers.UnitSpec
import scala.annotation.tailrec

class SetUpTradeDetailsUnitSpec extends UnitSpec {
  def countSubstring(str1:String, str2:String):Int={
    @tailrec def count(pos:Int, c:Int):Int={
      val idx=str1 indexOf(str2, pos)
      if(idx == -1) c else count(idx+str2.size, c+1)
    }
    count(0,0)
  }

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.SetUpTradeDetails.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.address))
    }

    "return a bad request if no details are entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "replace max length error message for traderBusinessName with exactly one error message (US158)" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> ("a" * 31),
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      val count = countSubstring(contentAsString(result), "Invalid characters are not allowed")
      count should equal(2)
    }

    "replace different error messages for traderBusinessName with exactly one error message (US158)" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> "",
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      val count = countSubstring(contentAsString(result), "Invalid characters are not allowed")
      count should equal(2) // The same message is displayed in 2 places - once in the validation-summary at the top of
      // the page and once above the field.
    }
  }
}