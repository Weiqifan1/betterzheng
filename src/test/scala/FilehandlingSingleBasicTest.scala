import DataObjects.ZhengmaPlain
import FilehandlingSingle.FilehandlingSingleBasic
import org.scalatest.funsuite.AnyFunSuite

class FilehandlingSingleBasicTest extends AnyFunSuite {

  test("read the custom singleCharacter files") {
    val filehandlerBasic = new FilehandlingSingleBasic
    val tradLines: List[String] = filehandlerBasic.readfile("src/resources/singleCharGenerated/trad13060.txt")
    val simpLies: List[String] = filehandlerBasic.readfile("src/resources/singleCharGenerated/simp9933.txt")

    val objListTrad: List[ZhengmaPlain] = filehandlerBasic.buildZhengmaPlain("src/resources/singleCharGenerated/trad13060.txt")
    val test2 = ""

    val withLongestCodes: List[ZhengmaPlain] = filehandlerBasic.produceLongestCodes(objListTrad)

    //test if the full kode list has a code with double w first and one with w last.
    //see if the full codes need to be corrected accordingly

    //446 hits. after manual test: a list of characters where the single a's SHOULD be removed
    val doRemoveSingleAsIfIndex: Set[Int] = Set(
      44,51,124,127,146,150,197,208,211,249,279,280,288,291,303,319,346,347,
      404,407,419,503,582,669,674,694,732,753,758,788,801,850,873,881,894,986,
      1070,1100,1127,1195,1209,1227,1290,1309,1355,1525,1536,1545,1777,1820,
      1868,1923,2044,2047,2245,2313,2352,2373,2395,2400,2472,2579,2595,2604,
      2654,2656,2689,2690,2869,2926,2948,2949,2954,2957,2968,2980,3034,
      )//naaet til index 152********************************************************************************************
    //test if final a (not aaa or aa) should be removed
    //aLongestCodeHasSingleA
    val singleAs: List[ZhengmaPlain] = withLongestCodes.filter(e => aLongestCodeHasSingleA(e.LongCodes)).toList


    //after manual test: a list of character where the double final a's SHOULD be removed
    //and the remains should be the longest code
    val doRemoveDoubleAsIfIndex: Set[Int] = Set(311,330,646,1228,1368,1388,1740,3451,4327,4408,4609,5319,
          5365,5865,5941,6293,6347,8362,9324,9369,10630,10639,11420,12761)
    val theFollovingShouldOnlyHaveOneARemoved: Set[Int] = Set(6713,1307)
    //test if final aa (but not aaa) should be removed
    val doubleAs: List[ZhengmaPlain] = withLongestCodes.filter(e => aLongestCodeHasDoubleAs(e.LongCodes)).toList

    //after manual test: if any longest code ends in aaa, then the A's should be removed,
    //and the single letter left should be the longest code
    //test if final aaa should be removed
    //val trippleAs: List[ZhengmaPlain] = withLongestCodes.filter(e => aLongestCodeHasTrippleAs(e.LongCodes)).toList

    //after manual test: a list of character where the single final v's SHOULD be removed
    val doRemoveSingleVsIfIndex: Set[Int] = Set(5,10,134,214,1635,1870,2880,3686,4119,4125,4892,9327)
    //find all lines where a longest code ends in v but not vv
    val withSingleV: List[ZhengmaPlain] = withLongestCodes.filter(e => anyCodeEndsInVButNotVV(e.LongCodes)).toList

    //after manual test: if any longest code ends in vv, then the v's should be removed
    //list all longest codes that end in vv
    //val withTwoV: List[ZhengmaPlain] = withLongestCodes.filter(e => anyCodeEndsInVV(e.LongCodes)).toList


    assert(tradLines.length == 13060)
    assert(simpLies.length == 9933)
  }

  def aLongestCodeHasSingleA(input: List[String]): Boolean = {
    val removeThoseWithAAA: List[String] = input.filter(e => !e.endsWith("aaa")).toList
    val removeThoseWithAA: List[String] = removeThoseWithAAA.filter(e => !e.endsWith("aa")).toList
    val anyWrongCode: Boolean = removeThoseWithAA.filter(e => e.endsWith("a")).toList.length>0
    return anyWrongCode
  }

  def aLongestCodeHasDoubleAs(input: List[String]): Boolean = {
    val removeThoseWithAAA: List[String] = input.filter(e => !e.endsWith("aaa")).toList
    val anyWrongCode: Boolean = removeThoseWithAAA.filter(e => e.endsWith("aa")).toList.length>0
    return anyWrongCode
  }

  def aLongestCodeHasTrippleAs(input: List[String]): Boolean = {
    val anyWrongCode = input.filter(e => e.endsWith("aaa")).toList.length>0
    return anyWrongCode
  }

  def anyCodeEndsInVButNotVV(input: List[String]): Boolean = {
    val removeThoseWithVV: List[String] = input.filter(e => !e.endsWith("vv")).toList
    val anyWrongCode: Boolean = removeThoseWithVV.filter(e => e.endsWith("v")).toList.length>0
    return anyWrongCode
  }

  def anyCodeEndsInVV(input: List[String]): Boolean = {
    val anyWrongCode = input.filter(e => e.endsWith("vv")).toList.length>0
    return anyWrongCode
  }

  def stringHasEnding(inputString: String, targetEnding: String): Boolean = {
    val ending: Boolean = inputString.endsWith(targetEnding)
    return ending
  }
}
