package Filehandling

import DataObjects.{CodeCollection, OriginSet, ZhengmaCharRaw}

import scala.collection.mutable.ListBuffer

class zhengmaFilehandler{

  def readfile(filePath: String): List[String] = {
    //val source = scala.io.Source.fromFile("src/resources/openvingenzmjd.dict.yaml")
    //val lines = try source.mkString finally source.close()
    val text: String = scala.io.Source.fromFile(filePath).mkString
    //scala.io.Source.fromFile("src/resources/openvingenzmjd.dict.yaml").mkString
    val lines: List[String] = text.split("\r\n").toList

    if (lines.length > 1) {
       return lines
    }else {
      val finallines = text.split("\n").toList
      return finallines
    }
    //val text2: String = scala.io.Source.fromFile("src/resources/zmjdtest.dict.yaml").mkString
    //val lines2: List[String] = text2.split("\r\n").toList
  }

  //case class ZhengmaCharRaw(char:String,
  //                          existsInTrad:Boolean,
  //                          existsInSimp:Boolean,
  //                          tzaiFreq:Int,
  //                          jundaFreq:Int,
  //                          zhengmaAllCodes:List[String],
  //                          zhengmaLongestCodes:List[String],
  //                          zhengmaOriginSet:OriginSet)
  //enum OriginSet:
  //  case Gongmu, Openvingen, Windows7

  def getListOfUnsortedRawObj: List[ZhengmaCharRaw] = {
    val sorted: List[ZhengmaCharRaw] = turnAllZhengmaFilesIntoRawObjects

    val mergeDublicates: List[ZhengmaCharRaw] = mergeListOfZhengmaObjRaw(sorted)

    return mergeDublicates

  }
  
  def turnAllZhengmaFilesIntoRawObjects: List[ZhengmaCharRaw] = {

    //gongmu phrases
    val gongmuPhrasePath: String = "src/resources/zhengmaSourceFiles/gongmu.bzzm.phrases.dict.yaml"
    val gongmuPhrases: List[String] = readfile(gongmuPhrasePath);
    val gongmuPhrasesNoBadLines: List[String] = gongmuPhrases.drop(29) //remove the first 28 lines
    val gongmuPhrasesNested: List[List[String]] = gongmuPhrasesNoBadLines.map(l => l.split("\t").toList)

    //gongmu phrases1
    val gongmuPhrase1Path: String = "src/resources/zhengmaSourceFiles/gongmu.bzzm.phrases1.dict.yaml"
    val gongmuPhrase1: List[String] = readfile(gongmuPhrase1Path)
    val gongmuPhrases1NoBadLines: List[String] = gongmuPhrase1.drop(29) //remove bad lines
    val gongmuPhrases1Nested: List[List[String]] = gongmuPhrases1NoBadLines.map(l => l.split("\t").toList)

    //gongmu words
    val gongmuWordsPath: String = "src/resources/zhengmaSourceFiles/gongmu.bzzm.words.dict.yaml"
    val gongmuWords: List[String] = readfile(gongmuWordsPath)
    val gongmuWordsNoBadLines: List[String] = gongmuWords.drop(33)
    val gongmuWordsNested: List[List[String]] = gongmuWordsNoBadLines.map(l => l.split("\t").toList)

    //openvingen big
    val openvingenPath: String = "src/resources/zhengmaSourceFiles/openvingen.zmbig.dict.yaml"
    val openvingen: List[String] = readfile(openvingenPath)
    val openvingenNoBadLines: List[String] = openvingen.drop(33)
    val openvingenNested: List[List[String]] = openvingenNoBadLines.map(l => l.split("\t").toList)

    //windows 7 TablTextServiceSimplifiedZhengma
    val windowsPath: String = "src/resources/zhengmaSourceFiles/windows7TableTextServiceSimplifiedZhengMa.txt"
    val windows: List[String] = readfile(windowsPath)
    val windowsNoBadLines: List[String] = windows.drop(92)
    //create an algoritm for splitting windows
    //val windowsSplit1: List[List[String]] = windowsNoBadLines.map(l => l.split("(\"=\")|(\",<\")").toList).toList
    val windowsSplit1: List[List[String]] = windowsNoBadLines.map(l => l.split("\"=\"").toList).toList
    val windowsSplit2: List[List[String]] = windowsSplit1.map(l => l.map(s=>s.split("\",<\"").toList).flatten)
    //remove quote sign and greater than, less than
    val windowSplit3: List[List[String]] = windowsSplit2.map(eachList => eachList.map(s=>s.replace("\"", ""))).toList
    val windowSplit4: List[List[String]] = windowSplit3.map(eachList => eachList.map(s=>s.replace(">", ""))).toList
    //move character lines to startOfList
    val windowsCharsFirst: List[List[String]] = windowSplit4.map(list=>(list.last :: list).distinct)

    //enum OriginSet:
    //  case Gongmu, Openvingen, Windows7

    //makeEachSetIntoRaws
    val gongmuPhrasesObjRaw: List[ZhengmaCharRaw] = nestedListToRawObj(gongmuPhrasesNested, OriginSet.Gongmu)
    val gongmuPhrases1ObjRaw = nestedListToRawObj(gongmuPhrases1Nested, OriginSet.Gongmu)
    val gongmuPhrasesWordsObjRaw = nestedListToRawObj(gongmuWordsNested, OriginSet.Gongmu)
    val openvingenObjRaw = nestedListToRawObj(openvingenNested, OriginSet.Openvingen)
    val windowsObjRaw = nestedListToRawObj(windowsCharsFirst, OriginSet.Windows7)

    //make them into a single list:
    val listOfRawObj: List[List[ZhengmaCharRaw]] = List(
      windowsObjRaw,
      gongmuPhrasesObjRaw,
      gongmuPhrases1ObjRaw,
      gongmuPhrasesWordsObjRaw,
      openvingenObjRaw
    )

    
    
    val combinedRawObj: List[ZhengmaCharRaw] = listOfRawObj.flatten.sortBy(_.char) //mergeListOfZhengmaObjRaw(listOfRawObj)


    return combinedRawObj

  }
  
  def mergeListOfZhengmaObjRaw(sortedRaw: List[ZhengmaCharRaw]): List[ZhengmaCharRaw] = {
    val findWo: List[ZhengmaCharRaw] = sortedRaw.filter(obj=> (obj.char == "我"))
    var cont = new ListBuffer[Int]()
    //for (i <- 0 to flatList.length-1) {
    //      val currentRawObj: ZhengmaCharRaw = flatList(i)
    //      if (currentRawObj.char == "我") {
    //        cont.addOne(i)
    //      }
    //    }


    return null
  }

  /** standard C-style for loop */
  /*
  inline def loop[A](
                      inline start: A,
                      inline condition: A => Boolean,
                      inline advance: A => A
                    )(inline loopBody: A => Any): Unit =
  var a = start
  while condition(a) do
    loopBody(a)
    a = advance(a)*/

  //(                         char:String,
  //                          existsInTrad:Boolean,
  //                          existsInSimp:Boolean,
  //                          tzaiFreq:Int,
  //                          jundaFreq:Int,
  //                          zhengmaAllCodes:List[CodeCollection],
  //                          zhengmaLongestCodes:List[String])
  //enum OriginSet:
  //  case Gongmu, Openvingen, Windows7

  def nestedListToRawObj(nestedZhengmaInput: List[List[String]], originSet: OriginSet): List[ZhengmaCharRaw] = {
    val result: List[ZhengmaCharRaw] = nestedZhengmaInput.map(eachList =>
      val allcodes: List[String] = eachList.drop(1)
      val listOfColl: List[CodeCollection] = List(CodeCollection(eachList.drop(1),originSet))
      ZhengmaCharRaw(eachList(0), false, false, 0, 0, listOfColl, List()))   //eachList.drop(1), null, originSet))
    return result
  }

  def textToCode: List[List[String]] = {
    //    val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    val allLines: List[String] = readfile("src/resources/zmjdtest.dict.yaml");
    val nested: List[List[String]] = allLines.map(l => l.split("\t").toList)
    return nested
  }


}
