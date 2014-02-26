package services.ordnance_survey.domain

import java.net.URI
import play.api.libs.json._

case class OSAddressbaseHeader(
                                uri: URI,
                                query: String,
                                offset: Int,
                                totalresults: Int,
                                format: String,
                                dataset: String,
                                maxresults: Int
                                )

object OSAddressbaseHeader {
  implicit val uriReads: Reads[URI] = new Reads[URI] {
    override def reads(json: JsValue) = JsSuccess(new URI(json.as[String]))
  }
  implicit val readsOSAddressbaseHeader = Json.reads[OSAddressbaseHeader]
}