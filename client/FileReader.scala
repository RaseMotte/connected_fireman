import java.io.File
import java.nio.file.{Files, Paths}

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

import scala.io.Source


object test {
  def main(args: Array[String]): Unit = {
    def exists(dir: String): Boolean = {
      Files.exists(Paths.get(dir))
    }

    def getListOfFiles(dir: File): List[File] = {
      dir.listFiles(_.isFile).toList
    }

    def checkExtension(fname: File) = fname.getName.drop(fname.getName.lastIndexOf(".")) match {
      case ".json" => readLine(fname)
      //case ".csv" => println(fname) // parseJson(fname)
      case _ => System.err.println(fname.getName + ": Bad file extension") // afficher erreur ?
    }

    def readLine(file: File) = {
      for (line <- Source.fromFile(file).getLines()) {

        val post = new HttpPost("http://localhost:9000/measurements")
        post.setHeader("Content-type", "application/json")
        post.setEntity(new StringEntity(line))

        val response = (new DefaultHttpClient).execute(post)


        //send in a thread or Future
        //postData(line)
        println(line)

      }
    }

    //def parseCsv(file: File): String

    // def parseJson(file: File)

    //def sendRequest(send: String) = {}

    def main_reader(directory: String) = {
      val dir = new File(directory)
      if (Files.exists(Paths.get(directory))) {

        val list = getListOfFiles(dir)

        list.foreach(f => checkExtension(f))

      }
    }

    main_reader("parsetest")

  }
}
