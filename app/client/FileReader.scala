import java.io.File
import java.nio.file.{Files, Paths}

import scalaj.http.{Http, HttpOptions}

import scala.io.Source

def exists(dir: String) : Boolean = {
  Files.exists(Paths.get(dir))
}

def getListOfFiles(dir: File): List[File] = { dir.listFiles(_.isFile).toList }

def checkExtension(fname: File) = fname.getName.drop(fname.getName.lastIndexOf(".")) match {
  case ".json" => readLine(fname)
  //case ".csv" => println(fname) // parseJson(fname)
  case _ => System.err.println(fname.getName +": Bad file extension") // afficher erreur ?
}

def readLine(file: File) = {
  for(line <- Source.fromFile(file).getLines()){
    val result = Http("http://localhost:9000/measurements").postData(line)
  .header("Content-Type", "application/json")
  .header("Charset", "UTF-8")
  .option(HttpOptions.readTimeout(10000)).asString
    print(result)
    //send in a thread or Future
    //postData(line)
    println(line)

  }
}

//def parseCsv(file: File): String

// def parseJson(file: File)

//def sendRequest(send: String) = {}

def main(directory: String) = {
  val dir = new File(directory)
  if (Files.exists(Paths.get(directory))) {

    val list = getListOfFiles(dir)

    list.foreach(f => checkExtension(f))

  }


}

main("parsetest")
