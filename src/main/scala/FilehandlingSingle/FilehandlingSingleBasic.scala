package FilehandlingSingle

class FilehandlingSingleBasic {


  def readfile(filePath: String): List[String] = {
    //val source = scala.io.Source.fromFile("src/resources/openvingenzmjd.dict.yaml")
    val text: String = scala.io.Source.fromFile(filePath).mkString
    val lines: List[String] = text.split("\n").toList
    return lines
  }

}
