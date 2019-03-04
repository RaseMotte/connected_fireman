package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class MeasurementController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def postData() = Action { implicit request: Request[AnyContent] =>
    println(request.body)
    Ok("bonjour")
  }


}