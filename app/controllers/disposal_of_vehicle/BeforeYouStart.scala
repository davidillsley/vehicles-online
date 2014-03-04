package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger

object BeforeYouStart extends Controller {

  def present = Action {
    implicit request =>
      val uniqueId = java.util.UUID.randomUUID.toString
      Logger.debug(s"BeforeYouStart - storing the following in session: modelId = $uniqueId")
      Ok(views.html.disposal_of_vehicle.before_you_start()).withSession("modelId" -> uniqueId)
  }

  def submit = Action { implicit request =>
    val modelId = request.session.get("modelId")
    Logger.debug(s"BeforeYouStart - reading modelId from session: $modelId")
    Redirect(routes.SetUpTradeDetails.present)
  }

}