package services.fakes

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import FakeDisposeWebServiceImpl._
import FakeVehicleLookupWebService._
import models.domain.disposal_of_vehicle._
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.Response
import services.dispose_service.DisposeWebService

class FakeDisposeWebServiceImpl extends DisposeWebService {
  override def callDisposeService(request: DisposeRequest): Future[Response] = Future {
    val disposeResponse: DisposeResponse = {
      request.referenceNumber match {
        case `simulateMicroServiceUnavailable` => throw new RuntimeException("simulateMicroServiceUnavailable")
        case `simulateSoapEndpointFailure` => disposeResponseSoapEndpointFailure
        case _ => disposeResponseSuccess
      }
    }
    val responseAsJson = Json.toJson(disposeResponse)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeDisposeWebServiceImpl {
  val transactionIdValid = "1234"
  val auditIdValid = "7575"
  val simulateMicroServiceUnavailable = "8" * 11
  val simulateSoapEndpointFailure = "9" * 11

  val disposeResponseSuccess =
    DisposeResponse(message = "Fake Web Dispose Service - Good response",
      transactionId = transactionIdValid,
      registrationNumber = registrationNumberValid,
      auditId = auditIdValid)

  val disposeResponseSoapEndpointFailure =
    DisposeResponse(message = "Fake Web Dispose Service - Bad response - Soap endpoint down",
      transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val disposeResponseFailureWithResponseCode =
    DisposeResponse(message = "Fake Web Dispose Service - Bad response",
      transactionId = transactionIdValid, // We should always get back a transaction id even for failure scenarios. Only exception is if the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.vehiclesService.response.unableToProcessApplication"))

  val disposeResponseSoapEndpointTimeout =
    DisposeResponse(message = "Fake Web Dispose Service - Bad response - Soap endpoint timeout",
      transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val disposeResponseApplicationBeingProcessed =
    DisposeResponse(message = "Fake Web Dispose Service - Good response",
      transactionId = transactionIdValid,
      registrationNumber = registrationNumberValid,
      auditId = auditIdValid,
      responseCode = None)

  val disposeResponseUnableToProcessApplication =
    DisposeResponse(message = "Fake Web Dispose Service - Bad response - Soap endpoint timeout",
      transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.vehiclesService.response.unableToProcessApplication"))

  val disposeResponseUndefinedError =
    DisposeResponse(message = "Fake Web Dispose Service - Bad response - Soap endpoint timeout",
      transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("undefined"))


  val consentValid = "true"
  val mileageValid = "20000"
}