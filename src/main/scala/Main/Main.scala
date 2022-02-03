//import filehandling.readfile
//import src.main.filehandling
//import com.jetbrains.betterzheng.src.main.filehandling
package Main

import FilehandlingRaw.zhengmaFilehandler
import FilehandlingSingle.FilehandlingSingleBasic
import java.util.logging.FileHandler

@main def helloWorld: Unit =
  println("Hello world!")
  val filehandlerBasic = new FilehandlingSingleBasic
  val tradLines = filehandlerBasic.readfile("src/resources/singleCharGenerated/trad13060.txt")
  val simpLies = filehandlerBasic.readfile("src/resources/singleCharGenerated/simp9933.txt")
  val test2 = ""


def msg = "I was compiled by Scala 3. :)"