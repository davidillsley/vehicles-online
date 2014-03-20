package models.domain.disposal_of_vehicle

import helpers.UnitSpec

class AddressViewModelSpec extends UnitSpec {

  "AddressViewModel - model" should {

    "handle a uprn of the correct size" in {
      val address = AddressViewModel(uprn = Some(10123456789L), address = Seq("line1", "line2", "line2"))

      val actualUprn = address.uprn.get
      actualUprn should equal(10123456789L)
      address.address.size should equal(3)
    }
  }
}