package controllers

import javax.inject._
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import services.{GpsDD, Measurement}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class MeasurementController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def postData = Action(parse.json) { request =>
    implicit val gpsDDFormat: Format[GpsDD] = Json.format[GpsDD]
    implicit val measurementFormat: Format[Measurement]= Json.format[Measurement]
    val measurementResult = request.body.validate[Measurement]
    measurementResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      measurement => {
        Logger.debug(s"Successfully build $measurement")
        measurement.save()
        Ok("Measurement added\n")
      }
    )
  }
}