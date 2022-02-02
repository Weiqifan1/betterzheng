
import DataObjects.ZhengmaCharRaw
import Filehandling.zhengmaFilehandler
import org.scalatest.funsuite.AnyFunSuite

class ZhengmaFileHandlerTest extends AnyFunSuite {

  //val text2: String = scala.io.Source.fromFile("src/resources/zmjdtest.dict.yaml").mkString
  //  val lines2: List[String] = text2.split("\r\n").toList
  val filehandler = new zhengmaFilehandler


  test("read zhengmafile and get correct length") {
    val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    assert(testtext.length == 2277)

  }

  test("convert zhengma line into nested list and check that all lines are clean") {
    //line 25-27 (starting at 1)
    //一	a
    //一下	aa
    //?	aaaa
    val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    val nestedList: List[List[String]] = filehandler.textToCode

    val line25: List[String] = nestedList(24)
    val line26: List[String] = nestedList(25)
    val line27: List[String] = nestedList(26)

    //一	a
    assert(line25.length == 2)
    assert(stringHasNoWhitespace(line25(0)))
    assert(stringHasNoWhitespace(line25(1)))
    assert(line25(0).length == 1)
    assert(line25(1).length == 1)

    //一下	aa
    assert(line26.length == 2)
    assert(stringHasNoWhitespace(line26(0)))
    assert(stringHasNoWhitespace(line26(1)))
    assert(line26(0).length == 2)
    assert(line26(1).length == 2)

    //?	aaaa
    assert(line27.length == 2)
    assert(stringHasNoWhitespace(line27(0)))
    assert(stringHasNoWhitespace(line27(1)))
    assert(line27(0).length == 1)
    assert(line27(1).length == 4)
    assert(line27(0).charAt(0).equals('?'))

    assert(nestedList.length == 2277)
  }


  test("turn all files into case objects") {
    //val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    //val nestedList: List[List[String]] = filehandler.textToCode

    val allFiles: List[ZhengmaCharRaw] = filehandler.getListOfUnsortedRawObj

    assert(true == false)

  }



  def stringHasNoWhitespace(input: String): Boolean = {
    val cleanString: String = input.strip()
    val stringHasSameLength: Boolean = input.size == cleanString.size
    return stringHasSameLength
  }



}