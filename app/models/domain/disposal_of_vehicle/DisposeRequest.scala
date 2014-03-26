package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class DisposeRequest(referenceNumber: String,
                        registrationNumber: String,
                        dateOfDisposal: String,
                        mileage: Option[Int])

object DisposeRequest {
  implicit val disposeRequestFormat = Json.format[DisposeRequest]
}