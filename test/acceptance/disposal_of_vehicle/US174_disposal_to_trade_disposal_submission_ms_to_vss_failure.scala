package acceptance.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import helpers.webbrowser._
import pages.disposal_of_vehicle.DisposePage
import pages.disposal_of_vehicle.DisposePage._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl.simulateSoapEndpointFailure

class US174_disposal_to_trade_disposal_submission_ms_to_vss_failure extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {
  private def cacheSetupStubsNoVssResponse() = {
    import helpers.disposal_of_vehicle.CacheSetup
    CacheSetup.setupTradeDetails().
      businessChooseYourAddress().
      vehicleDetailsModel().
      vehicleLookupFormModel(referenceNumber = simulateSoapEndpointFailure)
  }

  feature("US174: Disposal to Trade - Disposal Submission - MS-to-VSS failure") {
    info("As a Motor Trader")
    info("I want to be told when an error occurs submitting a disposal")
    info("So that I can choose whether to take other action")
    info("")

    scenario("VSS is unavailable") {
      new WebBrowser {
        Given("VSS is unavailable")
        cacheSetupStubsNoVssResponse()
        go to DisposePage
        dateOfDisposalDay select dateOfDisposalDayValid
        dateOfDisposalMonth select dateOfDisposalMonthValid
        dateOfDisposalYear select dateOfDisposalYearValid
        click on consent
        click on lossOfRegistrationConsent

        When("the motor trader has tried to submit the disposal")
        click on dispose

        Then("an error message is displayed")
        page.title should equal("We are sorry")

        And("they are given the option to either go back to the submission page or exit the service")
        page.text should include("Back")
        page.text should include("Exit")
      }
    }
  }
}
