import java.io.{File, FileWriter, PrintWriter}
import java.nio.file.{Files, Paths}

import scalaj.http.{Http, HttpOptions}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

object FileReader {

  def exists(dir: String): Boolean = {
    Files.exists(Paths.get(dir))
  }

  def getListOfFiles(dir: File): List[File] = {
    dir.listFiles(_.isFile).toList
  }

  def checkExtension(fname: File) = fname.getName.drop(fname.getName.lastIndexOf(".")) match {
    case ".json" => readLine(fname)
    case _ => System.err.println(fname.getName + ": Bad file extension")
  }

  def writeLine(line: String) = {
    val fw = new FileWriter("logs.txt", true)
    fw.write(line)
    fw.close()
  }

  def readLine(file: File) = {
    for (line <- Source.fromFile(file).getLines()) {
      if (line.contains("metric")) {
        val f = Future {
          Http("http://localhost:9000/emergency").postData(line)
            .header("Content-Type", "application/json").asString
        }
        f.onComplete {
          case Success(value) => writeLine(value.toString)
          case Failure(exception) =>writeLine(exception.toString)
        }
      }
      else {
        val f = Future {
          Http("http://localhost:9000/measurements").postData(line)
            .header("Content-Type", "application/json").asString
        }
        f.onComplete {
          case Success(value) => writeLine(value.toString)
          case Failure(exception) => writeLine(exception.toString)
        }
      }
      // Thread.sleep(5000) // To be realistic
    }
  }

  def main_read(directory: String) = {
    val dir = new File(directory)
    if (Files.exists(Paths.get(directory))) {

      val list = getListOfFiles(dir)

      list.foreach(f => checkExtension(f))

    }
    Thread.sleep(25000) // Mandatory to let the time to all requests to reach the server

  }

  def main(args: Array[String]): Unit = {
    main_read("../test")
  }

}