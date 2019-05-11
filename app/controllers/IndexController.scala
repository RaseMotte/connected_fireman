package controllers


import javax.inject._
import play.api.Logger
import play.api.db._
import play.api.libs.json._
import play.api.mvc._
import services.{Emergency, Measurement}


/**
 * Abstract controller used to implement the common view.
 * (Summary of the data)
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class IndexController @Inject()(cc: ControllerComponents,
  db: Database) extends AbstractController(cc) {

    def getData() = Action { implicit request: Request[AnyContent] =>
      val dbConnection = db.getConnection()
      try {
        val statement = dbConnection.createStatement()

        val measurements = Measurement.parseStream(
          statement.executeQuery(s"SELECT * FROM measurement;"),
          List[Measurement]())

        val emergencies = Emergency.parseStream(
          statement.executeQuery(s"SELECT * FROM emergency;"),
          List[Emergency]())

        Ok(views.html.index(measurements, emergencies))
      }
      finally {
        dbConnection.close()
      }
    }
  }
