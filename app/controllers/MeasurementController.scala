package controllers

import java.sql.ResultSet

import javax.inject._
import play.api.Logger
import play.api.db._
import play.api.libs.json._
import play.api.mvc._
import services.Measurement

import scala.annotation.tailrec



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

  @tailrec
  final def loop(res: ResultSet,
                 accumulator: String): String = {
    if (!res.next) "{ measurement: [" + accumulator.substring(0, accumulator.length() - 2) + "] }"
    else {
      val value = "{udid: " + res.getString("udid") +
        ", longitude: " + res.getString("longitude") +
        ", latitude: " + res.getString("latitude") +
        ", temperature in: " + res.getString("temperaturein") +
        ", temperature out: " + res.getString("temperatureout") +
        ", time: " + res.getString("mtime") + "},"
      loop(res, value + "\n" + accumulator)
    }
  }


  def getData() = Action { implicit request: Request[AnyContent] =>
    val dbConnection = db.getConnection()
    try {
      val statement = dbConnection.createStatement()

      val res = statement.executeQuery(s"SELECT * FROM measurement;")
      val to_return : String = loop(res, "")
      Ok(to_return)
    }
    finally {
      dbConnection.close()
    }
  }
}