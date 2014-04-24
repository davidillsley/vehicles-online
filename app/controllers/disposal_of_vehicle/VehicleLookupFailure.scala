package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import controllers.disposal_of_vehicle.Helpers._
import scala.Some

object VehicleLookupFailure extends Controller {
  def present = Action { implicit request =>
    (fetchDealerDetailsFromCache, fetchVehicleLookupDetailsFromCache) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) => {
        Logger.debug("found dealer and vehicle details")
        val responseCodeErrorMessage: String  = {
          fetchVehicleLookupResponseCodeFromCache match {
            case Some(responseCode) => responseCode
            case _ => "disposal_vehiclelookupfailure.p1"
          }
        }
        Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure(vehicleLookUpFormModelDetails, responseCodeErrorMessage))
      }
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (fetchDealerDetailsFromCache, fetchVehicleLookupDetailsFromCache) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) => {
        Logger.debug("found dealer and vehicle details")
        Redirect(routes.VehicleLookup.present)
      }
      case _ => Redirect(routes.BeforeYouStart.present)
    }
  }
}


