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
      if (line.contains("metrics")) {
        val f = Future { Http("http://localhost:9000/emergency").postData(line)
          .header("Content-Type", "application/json")
          .options(HttpOptions.readTimeout(10000)).asString }
        f.onComplete {
          case Success(value) => new PrintWriter("client/logs.txt") { write(value.toString); close }
          case Failure(exception) => new PrintWriter("client/logs.txt") { write(exception.toString); close }
        }
      }
      else {
        val f = Future {
          Http("http://localhost:9000/measurements").postData(line)
            .header("Content-Type", "application/json")
            .options(HttpOptions.readTimeout(10000)).asString
        }
        f.onComplete {
          case Success(value) => new PrintWriter("client/logs.txt") { write(value.toString); close }
          case Failure(exception) => new PrintWriter("client/logs.txt") { write(exception.toString); close }
        }
      }
  }
}

def main(directory: String) = {
  val dir = new File(directory)
  if (Files.exists(Paths.get(directory))) {

    val list = getListOfFiles(dir)

    list.foreach(f => checkExtension(f))

  }


}

main("test")
