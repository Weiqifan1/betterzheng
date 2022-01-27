//import filehandling.readfile
//import src.main.filehandling
//import com.jetbrains.betterzheng.src.main.filehandling
package Main

import Filehandling.Filehandler

import java.util.logging.FileHandler

@main def helloWorld: Unit =
  println("Hello world!")
  val filehandler = new Filehandler
  val testvar: List[String] = filehandler.readfile("src/resources/openvingenzmjd.dict.yaml");
  println(testvar)
  println(msg)

def msg = "I was compiled by Scala 3. :)"