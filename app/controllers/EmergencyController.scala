package controllers

import java.sql.ResultSet

import javax.inject._
import play.api.Logger
import play.api.db._
import play.api.libs.json._
import play.api.mvc._
import services.Emergency

import scala.annotation.tailrec



/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class EmergencyController @Inject()(cc: ControllerComponents,
                                      db: Database) extends AbstractController(cc) {

  def postData = Action(parse.json) { request =>
    val dbConnection = db.getConnection()
    implicit val emergencyFormat: Format[Emergency]= Json.format[Emergency]
    val EmergencyResult = request.body.validate[Emergency]
    EmergencyResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "422", "message" -> JsError.toJson(errors)))
      },
      emergency => {
        Logger.debug(s"Successfully build $emergency")
        try {
          val statement = dbConnection.createStatement()
          println(s"INSERT INTO emergency VALUES ${emergency.sqlFormat};")
          val res = statement.executeUpdate(s"INSERT INTO emergency VALUES ${emergency.sqlFormat};")
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
    if (!res.next) "{ emergency: [" + accumulator.substring(0, accumulator.length()) + "] }"
    else {
      val value = "{udid: " + res.getString("udid") +
        ", metric: " + res.getString("metric") +
        ", message: " + res.getString("message") +
        ", time: " + res.getString("mtime") + "},"
      loop(res, value + "\n" + accumulator)
    }
  }


  def getData() = Action { implicit request: Request[AnyContent] =>
    val dbConnection = db.getConnection()
    try {
      val statement = dbConnection.createStatement()

      val res = statement.executeQuery(s"SELECT * FROM emergency;")
      val to_return : String = loop(res, "")
      Ok(to_return)
    }
    finally {
      dbConnection.close()
    }
  }
}