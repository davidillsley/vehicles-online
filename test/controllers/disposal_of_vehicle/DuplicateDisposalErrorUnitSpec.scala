package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import helpers.{UnitSpec, WithApplication}
import org.mockito.Mockito._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.helpers.Config

final class DuplicateDisposalErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      status(present) should equal(OK)
    }

    "not display progress bar" in new WithApplication {
      contentAsString(present) should not include "Step "
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }

    "not display prototype message when config set to false" in new WithApplication {
      val request = FakeRequest()
      implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      implicit val config: Config = mock[Config]
      when(config.isPrototypeBannerVisible).thenReturn(false) // Stub this config value.
      val duplicateDisposalErrorPrototypeNotVisible = new DuplicateDisposalError()

      val result = duplicateDisposalErrorPrototypeNotVisible.present(request)
      contentAsString(result) should not include """<div class="prototype">"""
    }
  }

  private lazy val present = {
    val duplicateDisposalError = injector.getInstance(classOf[DuplicateDisposalError])
    val newFakeRequest = FakeRequest()
    duplicateDisposalError.present(newFakeRequest)
  }
}
