package FilehandlingRaw

import DataObjects.{CodeCollection, OriginSet, ZhengmaCharRaw}

import java.io.{BufferedWriter, FileOutputStream, FileWriter, OutputStreamWriter}
import scala.collection.immutable.HashMap
import scala.collection.mutable
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

  def loadAllTradChar: List[String] = {
    val testtext: List[String] = readfile("src/resources/dictionarySourceFiles/tsaifrequency.txt")
    //13060
    val removeNonChinese: List[String] = testtext.map(x=>x.split("\\s")(0)).toList.drop(11)
    return removeNonChinese
  }

  def loadAllSimpChar: List[String] = {
    val testtext: List[String] = readfile("src/resources/dictionarySourceFiles/jundafrequency.txt")
    //13060
    val removeNonChinese: List[String] = testtext.drop(20).map(x=>x.split("\\s")(1))
    return removeNonChinese
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
    //length 1093304
    //val test02 = sortedRaw(20302)
    //val findWo: List[ZhengmaCharRaw] = sortedRaw.filter(obj=> (obj.char == "我"))
    val testExamples: List[ZhengmaCharRaw] = List(
      sortedRaw(20302),sortedRaw(20303),sortedRaw(20304),sortedRaw(20305),sortedRaw(20306),sortedRaw(20307),sortedRaw(20308),sortedRaw(20309),sortedRaw(20310),sortedRaw(20311),
      sortedRaw(20312),sortedRaw(20313),sortedRaw(20314),sortedRaw(20315),sortedRaw(20316),sortedRaw(20317),sortedRaw(20318),sortedRaw(20319),sortedRaw(20320),sortedRaw(20321)
    )

    //get a list of all strings/char in the collection
    //val allString: List[String] = testExamples.map(elem => elem.char).distinct
    //val preMerged: List[List[ZhengmaCharRaw]] = allString.map(eachStr =>
    //  testExamples.filter(eachObj => eachObj.char == eachStr)
    //)
    val allString: List[String] = sortedRaw.map(elem => elem.char).distinct
    val preMerged: List[List[ZhengmaCharRaw]] = allString.map(eachStr =>
      sortedRaw.filter(eachObj => eachObj.char == eachStr)
    )


    //char:String,
    //                          existsInTrad:Boolean,
    //                          existsInSimp:Boolean,
    //                          tzaiFreq:Int,
    //                          jundaFreq:Int,
    //                          zhengmaAllCodes:List[CodeCollection],
    //                          zhengmaLongestCodes:List[String])



    //create a true merge
    val merged: List[ZhengmaCharRaw] = preMerged.map(eachList =>
      val codes: List[CodeCollection] = eachList.map(c => c.zhengmaAllCodesWithTypes(0))
      ZhengmaCharRaw(
        eachList(0).char, eachList(0).existsInTrad, eachList(0).existsInSimp, eachList(0).tzaiFreq, eachList(0).jundaFreq,
        codes,codes.map(x=>x.codes).flatten.distinct, List()))

    //create a function that can take the list of CodeCollections, and return the longest code
    //(but such that codes that end in a,aa,aaa or vv and has a shorter version that doesnt have the letters, is not counted).




    return merged
  }

  def nestedListToRawObj(nestedZhengmaInput: List[List[String]], originSet: OriginSet): List[ZhengmaCharRaw] = {
    val result: List[ZhengmaCharRaw] = nestedZhengmaInput.map(eachList =>
      val allcodes: List[String] = eachList.drop(1)
      val listOfColl: List[CodeCollection] = List(CodeCollection(eachList.drop(1),originSet))
      ZhengmaCharRaw(eachList(0), false, false, 0, 0, listOfColl,listOfColl.map(x=>x.codes).flatten.distinct, List()))   //eachList.drop(1), null, originSet))
    return result
  }

  def textToCode: List[List[String]] = {
    //    val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    val allLines: List[String] = readfile("src/resources/zmjdtest.dict.yaml");
    val nested: List[List[String]] = allLines.map(l => l.split("\t").toList)
    return nested
  }

  def getListOfUnsortedRawObj: List[ZhengmaCharRaw] = {
    val sorted: List[ZhengmaCharRaw] = turnAllZhengmaFilesIntoRawObjects

    val loadAllTrad: List[String] = loadAllTradChar
    val tradIndex: List[Int] = (1 to loadAllTrad.length).toList
    val tradMap: Map[String, Int] = (loadAllTrad zip tradIndex).toMap
    val tradSet: Set[String] = loadAllTrad.toSet
    val loadAllSimp: List[String] = loadAllSimpChar
    val simpIndex: List[Int] = (1 to loadAllSimp.length).toList
    val simpMap: Map[String,Int] = (loadAllSimp zip simpIndex).toMap
    val simpSet: Set[String] = loadAllSimp.toSet

    //trad single

    val onlyTrad: List[ZhengmaCharRaw] = sorted.filter(each=>tradSet.contains(each.char))
    val mergeDublicates: List[ZhengmaCharRaw] = mergeListOfZhengmaObjRaw(onlyTrad)
    val withFreq: List[ZhengmaCharRaw] = mergeDublicates.map(each=> each.copy(tzaiFreq = tradMap(each.char)))
    val tradToString: List[String] = withFreq.sortBy(_.tzaiFreq).map(x=>
      (x.char + "\t" + x.tzaiFreq + "\t" + x.zhengmaAllCodes.mkString(",") + "\n"))
    val filen: String = "trad13060.txt"
    val writer = new BufferedWriter(new FileWriter(filen))
    tradToString.foreach(writer.write)
    writer.close()
    //val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filen)))
    //tradToString.foreach(writer.write)


    //simp single
    /*
    val onlySimp: List[ZhengmaCharRaw] = sorted.filter(each=>simpSet.contains(each.char))
    val mergeDublicates: List[ZhengmaCharRaw] = mergeListOfZhengmaObjRaw(onlySimp)
    val withFreq: List[ZhengmaCharRaw] = mergeDublicates.map(each=> each.copy(jundaFreq = simpMap(each.char)))
    val simpToString: List[String] = withFreq.sortBy(_.jundaFreq).map(x=>
      (x.char + "\t" + x.jundaFreq + "\t" + x.zhengmaAllCodes.mkString(",") + "\n"))
    val filen: String = "simp9933.txt"
    val writer = new BufferedWriter(new FileWriter(filen))
    simpToString.foreach(writer.write)
    writer.close()
    */
    //val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filen)))
    //simpToString.foreach(writer.write)

    //der mangler nogle tegn
    //9453


    return mergeDublicates

  }



}

/*
var preMerged = ListBuffer[ListBuffer[ZhengmaCharRaw]]()
    var elemColl: mutable.Seq[ZhengmaCharRaw] = ListBuffer[ZhengmaCharRaw]()
    var oldElem: ZhengmaCharRaw = null
    var newElem: ZhengmaCharRaw = null
    for (index <- 0 to sortedRaw.length-1) {
      newElem = sortedRaw(index)
      if (index == 0) {
        elemColl.appended(newElem)
      }else if (index == sortedRaw.length-1) {
        if (oldElem.char == newElem.char) {
          elemColl.appended(newElem)
          preMerged.appended(elemColl)
        }else {
          preMerged.appended(elemColl)
          elemColl = ListBuffer[ZhengmaCharRaw]()
          elemColl.appended(newElem)
          preMerged.appended(elemColl)
        }
      }else {
        if (oldElem.char == newElem.char) {
          elemColl.appended(newElem)
        }else{
          preMerged.appended(elemColl)
          elemColl = ListBuffer[ZhengmaCharRaw]()
          elemColl.appended(newElem)
        }
      }
      oldElem = newElem
    }
*/
