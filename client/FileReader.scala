import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

import scalaj.http.{Http, HttpOptions}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

def exists(dir: String) : Boolean = {
  Files.exists(Paths.get(dir))
}

def getListOfFiles(dir: File): List[File] = { dir.listFiles(_.isFile).toList }

def checkExtension(fname: File) = fname.getName.drop(fname.getName.lastIndexOf(".")) match {
  case ".json" => readLine(fname)
  case _ => System.err.println(fname.getName +": Bad file extension")
}


def readLine(file: File) = {
  for(line <- Source.fromFile(file).getLines()){
      if (line.contains("metric")) {
        val f = Future { Http("http://localhost:9000/emergency").postData(line)
          .header("Content-Type", "application/json").asString }
        f.onComplete {
          case Success(value) => reflect.io.File("logs.txt").writeAll(value.toString)
          case Failure(exception) => reflect.io.File("logs.txt").writeAll(exception.toString)
        }
      }
      else {
        val f = Future {
          Http("http://localhost:9000/measurements").postData(line)
            .header("Content-Type", "application/json").asString
        }
        f.onComplete {
          case Success(value) => reflect.io.File("logs.txt").writeAll(value.toString)
          case Failure(exception) => reflect.io.File("logs.txt").writeAll(exception.toString)
        }
      }
    // Thread.sleep(5000) // To be realistic
  }
}

def main(directory: String) = {
  val dir = new File(directory)
  if (Files.exists(Paths.get(directory))) {

    val list = getListOfFiles(dir)

    list.foreach(f => checkExtension(f))

  }
  Thread.sleep(25000) // Mandatory to let the time to all requests to reach the server

}

main("test")
