import FilehandlingSingle.FilehandlingSingleBasic
import org.scalatest.funsuite.AnyFunSuite

class FilehandlingSingleBasicTest extends AnyFunSuite {

  test("read the custom singleCharacter files") {
    val filehandlerBasic = new FilehandlingSingleBasic
    val tradLines: List[String] = filehandlerBasic.readfile("src/resources/singleCharGenerated/trad13060.txt")
    val simpLies: List[String] = filehandlerBasic.readfile("src/resources/singleCharGenerated/simp9933.txt")
    val test2 = ""

    val tradLinesUniqueLongestCodes: List[String] = filehandlerBasic.getNonUniqueLongestCodelines(tradLines)

    assert(tradLinesUniqueLongestCodes.length > 0)
    assert(tradLines.length == 13060)
    assert(simpLies.length == 9933)

  }
}
