import java.io.File
import java.nio.file.{Files, Paths}
/*
def exists(dir: String) : Boolean = {
  Files.exists(Paths.get(dir))
}

def getListOfFiles(dir: File): List[File] = { dir.listFiles(_.isFile).toList }

def checkExtension(fname: File) = fname.getName.drop(fname.getName.lastIndexOf(".")) match {
  case ".json" => println(fname) // parseCsv()
  case ".csv" => println(fname) // parseJson()
  case _ => System.err.println("Bad file extension")
}

// def parseCsv(file: File)

// def parseJson(file: File)

def main(directory: String) = {
  val dir = new File(directory)
  println(dir.exists())
  println(dir.isDirectory)
  println(Files.exists(Paths.get(directory)))
  if (Files.exists(Paths.get(directory))) {

    val list = getListOfFiles(dir)

    list.foreach(f => checkExtension(f))

  }


}

main("parsetest")*/
