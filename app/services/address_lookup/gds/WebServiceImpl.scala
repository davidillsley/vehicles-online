package services.address_lookup.gds

import play.api.libs.ws.{Response, WS}
import scala.concurrent.Future
import play.api.Logger
import utils.helpers.Config
import services.address_lookup.AddressLookupWebService

final class WebServiceImpl extends AddressLookupWebService {
  private val baseUrl: String = Config.gdsAddressLookupBaseUrl
  private val authorisation: String = Config.gdsAddressLookupAuthorisation
  private val requestTimeout: Int = Config.gdsAddressLookupRequestTimeout

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  // request should look like    (GET, "/addresses?postcode=kt70ej").withHeaders(validAuthHeader)
  override def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/addresses?postcode=${ postcodeWithNoSpaces(postcode) }"
    Logger.debug(s"Calling GDS postcode lookup service on $endPoint...")
    WS.url(endPoint).
      withHeaders("AUTHORIZATION" -> authorisation).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String): Future[Response] = {
    val endPoint = s"$baseUrl/uprn?uprn=$uprn"
    Logger.debug(s"Calling GDS uprn lookup service on $endPoint...")
    WS.url(endPoint).
      withHeaders("AUTHORIZATION" -> authorisation).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }
}
