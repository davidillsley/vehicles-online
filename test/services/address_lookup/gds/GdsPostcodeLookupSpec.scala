package services.address_lookup.gds

import services.fakes.{FakeResponse, FakeWebServiceImpl}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.{AddressLookupService, gds}
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import play.api.libs.ws.Response
import services.address_lookup.gds.domain.{Presentation, Details, Location, Address}
import play.api.libs.json.{JsValue, Json}
import services.address_lookup.gds.domain.JsonFormats.addressFormat
import helpers.UnitSpec
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Second, Span}
import services.fakes.FakeWebServiceImpl.{uprnValid, gdsAddress}

class GdsPostcodeLookupSpec extends UnitSpec {
  /*
    The service will:
    1) Send postcode string to GDS micro-service
    2) Get a response from the GDS micro-service
    3) Translate the response into a Seq that can be used by the drop-down
    */
  def addressServiceMock(response: Future[Response]): AddressLookupService = {
    // Using the real address lookup service but passing in a fake web service that returns the responses we specify.
    new gds.AddressLookupServiceImpl(new FakeWebServiceImpl(responseOfPostcodeWebService = response, responseOfUprnWebService = response))
  }

  def response(statusCode: Int, inputAsJson: JsValue) = Future {
    FakeResponse(status = statusCode, fakeJson = Some(inputAsJson))
  }

  def responseThrows = Future {
    val response = mock[Response]
    when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
    response
  }

  def responseTimeout = Future {
    val response = mock[Response]
    when(response.status).thenThrow(new java.util.concurrent.TimeoutException("This error is generated deliberately by a test"))
    response
  }

  "fetchAddressesForPostcode" should {
    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMock(responseTimeout)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result, Timeout(Span(1, Second))) { _ shouldBe empty }
    }

    "return empty seq when response throws" in {
      val service = addressServiceMock(responseThrows)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result, Timeout(Span(1, Second))) { _ shouldBe empty }
    }

    "return empty seq when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }

    "return empty seq when micro-service response status is not 200 (OK)" in {
      val input: Seq[Address] = Seq(gdsAddress())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(404, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }


    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      val expectedResults: Seq[Address] = Seq.empty
      val inputAsJson = Json.toJson(expectedResults)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }

    "return seq of (uprn, address) when micro-service returns a single address" in {
      val expected = (uprnValid, "property stub, 123, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(gdsAddress())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe Seq(expected) }
    }

    "return seq of (uprn, address) when micro-service returns many addresses" in {
      val expected = (uprnValid, "property stub, 123, town stub, area stub, postcode stub")
      val input = Seq(gdsAddress(), gdsAddress(), gdsAddress())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe Seq(expected, expected, expected) }
    }

    "not throw when an address contains a building number that contains letters" in {
      val expected = Seq(
        (uprnValid, "property stub, 789C, town stub, area stub, postcode stub"),
        (uprnValid, "presentationProperty BBB, 123B, town stub, area stub, postcode stub"),
        (uprnValid, "presentationProperty AAA, 123A, town stub, area stub, postcode stub")
      )
      val input = Seq(
        gdsAddress(presentationStreet = "789C"),
        gdsAddress(presentationProperty = "presentationProperty BBB", presentationStreet = "123B"),
        gdsAddress(presentationProperty = "presentationProperty AAA", presentationStreet = "123A")
      )
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe expected }
    }

    "return seq of (uprn, address) sorted by building number then building name" in {
      val expected = Seq(
        (uprnValid, "property stub, 789, town stub, area stub, postcode stub"),
        (uprnValid, "presentationProperty BBB, 123, town stub, area stub, postcode stub"),
        (uprnValid, "presentationProperty AAA, 123, town stub, area stub, postcode stub")
      )
      val input = Seq(
        gdsAddress(presentationStreet = "789"),
        gdsAddress(presentationProperty = "presentationProperty BBB", presentationStreet = "123"),
        gdsAddress(presentationProperty = "presentationProperty AAA", presentationStreet = "123")
      )
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe expected }
    }
  }

  "fetchAddressForUprn" should {
    "return None when cannot connect to micro-service" in {
      val service = addressServiceMock(responseTimeout)

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when response throws" in {
      val service = addressServiceMock(responseThrows)

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when micro-service response status is not 200 (OK)" in {
      val input: Seq[Address] = Seq(gdsAddress())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(404, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when micro-service returns empty seq (meaning no addresses found)" in {
      val inputAsJson = Json.toJson(Seq.empty)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return AddressViewModel when micro-service returns a single address" in {
      val expected = Seq("presentationProperty stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(gdsAddress())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        case Some(addressViewModel) => {
          addressViewModel.uprn should equal(Some(uprnValid.toLong))
          println("addressViewModel.address: " + addressViewModel.address)
          println("expected: " + expected)
          addressViewModel.address === expected
        }
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return AddressViewModel of the first in the seq when micro-service returns many addresses" in {
      val expected = Seq("presentationProperty stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(gdsAddress(), gdsAddress(), gdsAddress())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        case Some(addressViewModel) => {
          addressViewModel.uprn should equal(Some(uprnValid.toLong))
          addressViewModel.address === expected
        }
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }
  }

}