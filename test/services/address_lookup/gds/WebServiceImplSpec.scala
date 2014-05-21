package services.address_lookup.gds

import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._

final class WebServiceImplSpec extends UnitSpec {
   "postcodeWithNoSpaces" should {
     val addressLookupService = new services.address_lookup.gds.WebServiceImpl()

     "return the same string if no spaces present" in {
       val result = addressLookupService.postcodeWithNoSpaces(postcodeValid)

       result should equal(postcodeValid)
     }

     "remove spaces when present" in {
       val result = addressLookupService.postcodeWithNoSpaces(postcodeValidWithSpace)

       result should equal(postcodeValid)
     }
   }
 }
