package controllers

import java.sql.ResultSet

import javax.inject._
import play.api.Logger
import play.api.db._
import play.api.libs.json._
import play.api.mvc._
import services.Measurement



/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class MeasurementController @Inject()(cc: ControllerComponents,
                                      db: Database) extends AbstractController(cc) {

  def postData = Action(parse.json) { request =>
    val dbConnection = db.getConnection()
    implicit val measurementFormat: Format[Measurement]= Json.format[Measurement]
    val measurementResult = request.body.validate[Measurement]
    println(request)
    println(request.headers)
    println(request.body)
    measurementResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "422", "message" -> JsError.toJson(errors)))
      },
      measurement => {
        Logger.debug(s"Successfully build $measurement")
        try {
          val statement = dbConnection.createStatement()
          println(s"INSERT INTO measurement VALUES ${measurement.sqlFormat};")
          val res = statement.executeUpdate(s"INSERT INTO measurement VALUES ${measurement.sqlFormat};")
          Ok("Measurement added\n")
        //} catch  {
          //BadRequest("status" -> "500", "message" -> "Failed add db")
          //Logger.error("Failed to add measurement to the db.")
        } finally {
          dbConnection.close()
        }
      }
    )
  }

    def getData() = Action { implicit request: Request[AnyContent] =>
      val dbConnection = db.getConnection()
      try {
        val statement = dbConnection.createStatement()

        val measurements = Measurement.parseStream(
          statement.executeQuery(s"SELECT * FROM measurement;"),
          List[Measurement]())

        Ok(views.html.index(measurements, Nil))
      }
      finally {
        dbConnection.close()
      }
    }
}
