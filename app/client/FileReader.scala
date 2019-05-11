import java.io.File
import java.nio.file.{Files, Paths}

import scala.io.Source

def exists(dir: String) : Boolean = {
  Files.exists(Paths.get(dir))
}

def getListOfFiles(dir: File): List[File] = { dir.listFiles(_.isFile).toList }

def checkExtension(fname: File) = fname.getName.drop(fname.getName.lastIndexOf(".")) match {
  case ".json" => readLine(fname)
  //case ".csv" => println(fname) // parseJson(fname)
  case _ => System.err.println(fname.getName +": Bad file extension")
}

def readLine(file: File) = {
  for(line <- Source.fromFile(file).getLines()){
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
  //val test = new MeasurementController()
  if (Files.exists(Paths.get(directory))) {

    val list = getListOfFiles(dir)

    list.foreach(f => checkExtension(f))

  }


}

main("parsetest")
