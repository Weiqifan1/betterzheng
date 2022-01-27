
import Filehandling.Filehandler
import org.scalatest.funsuite.AnyFunSuite

class FileHandlerTest extends AnyFunSuite {

  //val text2: String = scala.io.Source.fromFile("src/resources/zmjdtest.dict.yaml").mkString
  //  val lines2: List[String] = text2.split("\r\n").toList
  val filehandler = new Filehandler


  test("multiplication with 0 should always give 0") {
    val testtext: List[String] = filehandler.readfile("src/resources/zmjdtest.dict.yaml")
    assert(testtext.length == 2277)

  }

}