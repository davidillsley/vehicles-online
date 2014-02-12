package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.Postcode._
import play.api.Logger
import play.api.Play.current

object SetUpTradeDetails extends Controller {

  val traderLookupForm = Form(
    mapping(
      traderBusinessNameId -> nonEmptyText(minLength = 2, maxLength = 100),
      traderPostcodeId -> postcode()
    )(SetupTradeDetailsModel.apply)(SetupTradeDetailsModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.setup_trade_details(traderLookupForm))
  }

  def submit = Action {
    implicit request => {
      traderLookupForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithErrors)),
        f => {
          val key = mappings.disposal_of_vehicle.SetupTradeDetails.traderBusinessNameId
          play.api.cache.Cache.set(key, f.traderBusinessName)
          Logger.debug(s"SetUpTradeDetails set value for key: $key")
          Redirect(routes.BusinessChooseYourAddress.present)
        }
      )
    }
  }
}