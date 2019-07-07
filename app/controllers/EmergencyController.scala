package controllers

import java.sql.ResultSet

import javax.inject._
import play.api.Logger
import play.api.db._
import play.api.libs.json._
import play.api.mvc._
import services.{Consumer, Emergency, Producer}



/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class EmergencyController @Inject()(cc: ControllerComponents,
                                      db: Database) extends AbstractController(cc) {

  def postData = Action(parse.json) { request =>
    val dbConnection = db.getConnection()
    val producer = Producer("kafka_stream", "key", request.body.toString())

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

    def getData() = Action { implicit request: Request[AnyContent] =>
      //val dbConnection = db.getConnection()
      val consumer = Consumer("kafka_stream")
      try {
        /*val statement = dbConnection.createStatement()

        val emergencies = Emergency.parseStream(
          statement.executeQuery(s"SELECT * FROM emergency;"),
          List[Emergency]())*/

        implicit val emergencyFormat: Format[Emergency]= Json.format[Emergency]
        val emergencies2 = consumer.get_list().foldLeft(List[Emergency]())((acc, elem) => acc ++ List(Json.parse(elem.value()).validate[Emergency].fold(errors => Emergency(0, "", "", ""), emergency => emergency)))
        println(emergencies2.toString())

        Ok(views.html.index(Nil, emergencies2))
      }
      /*finally {
        dbConnection.close()
      }*/
    }
}
