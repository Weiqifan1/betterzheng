package FilehandlingSingle

class FilehandlingSingleBasic {


  def readfile(filePath: String): List[String] = {
    //val source = scala.io.Source.fromFile("src/resources/openvingenzmjd.dict.yaml")
    val text: String = scala.io.Source.fromFile(filePath).mkString
    val lines: List[String] = text.split("\n").toList
    return lines
  }

  def getNonUniqueLongestCodelines(zhengmaLines: List[String]): List[String] = {
    //get lines where there are two longest codes
    val doubleLongestCodes: List[String] = zhengmaLines.filter(!onlyOneLongestCode(_))
    //get conflicting longest lines

    //val loadAllTrad: List[String] = loadAllTradChar
    //    val tradIndex: List[Int] = (1 to loadAllTrad.length).toList
    //    val tradMap: Map[String, Int] = (loadAllTrad zip tradIndex).toMap

    val mapobj = mapOfCodesToLineList(doubleLongestCodes)

    return doubleLongestCodes;
  }

  def mapOfCodesToLineList(linesToCreateMap: List[String]): Map[String, List[String]] = {
    val allUniqueCodes: List[String] = linesToCreateMap.map(e => getLongestCodesFromLine(e)).flatten.distinct

    return null

  }

  //def getAllCodes(lines: List[String]): List[String]

  def getLongestCodesFromLine(line: String): List[String] = {
    val allelems: List[String] = line.split("\t").toList
    val lineCodes: List[String] = allelems(2).strip().split(",").sortBy(_.length).toList
    if (lineCodes.length < 1) {
      println(allelems)
      return null
    }else if (lineCodes.length < 2 || onlyOneLongestCode(line)){
      return List(lineCodes(lineCodes.length-1))
    }else{
      val lengthOfLongestOcde: Int = lineCodes(lineCodes.length-1).length
      val longestCodes: List[String] = lineCodes.filter(_.length == lengthOfLongestOcde)
      return longestCodes
    }

  }

  def onlyOneLongestCode(line: String): Boolean = {
    val allelems: List[String] = line.split("\t").toList
    val lineCodes: List[String] = allelems(2).strip().split(",").sortBy(_.length).toList
    if (lineCodes.length < 1) {
      println(allelems)
      return false
    }else if (lineCodes.length < 2){
      return true
    }else{
      val last: String = lineCodes(lineCodes.length-1)
      val secondToLast: String = lineCodes(lineCodes.length-2)
      if (last.length == secondToLast.length) {
        return false
      }else {
        return true
      }
    }
  }
}
