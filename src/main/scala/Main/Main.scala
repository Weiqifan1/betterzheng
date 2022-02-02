//import filehandling.readfile
//import src.main.filehandling
//import com.jetbrains.betterzheng.src.main.filehandling
package Main

import Filehandling.zhengmaFilehandler

import java.util.logging.FileHandler

@main def helloWorld: Unit =
  println("Hello world!")
  val filehandler = new zhengmaFilehandler
  val testvar: List[String] = filehandler.readfile("src/resources/dictionarySourceFiles/cedict_ts.u8");
  println(testvar.length)
  println(msg)

def msg = "I was compiled by Scala 3. :)"