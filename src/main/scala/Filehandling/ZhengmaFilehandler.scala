package Filehandling

class ZhengmaFilehandler{

  def readfile(filePath: String): List[String] = {
    //val source = scala.io.Source.fromFile("src/resources/openvingenzmjd.dict.yaml")
    //val lines = try source.mkString finally source.close()
    val text: String = scala.io.Source.fromFile(filePath).mkString
    //scala.io.Source.fromFile("src/resources/openvingenzmjd.dict.yaml").mkString
    val lines: List[String] = text.split("\r\n").toList

    val text2: String = scala.io.Source.fromFile("src/resources/zmjdtest.dict.yaml").mkString
    val lines2: List[String] = text2.split("\r\n").toList

    val finaltext = scala.io.Source.fromFile(filePath).mkString
    val finallines = finaltext.split("\r\n").toList


    return finallines
  }

  def textToCode: List[List[String]] = {
    //    val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    val allLines: List[String] = readfile("src/resources/zmjdtest.dict.yaml");
    val nested: List[List[String]] = allLines.map(l => l.split("\t").toList)
    return nested
  }


}