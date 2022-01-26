//import filehandling.readfile
//import src.main.filehandling
//import com.jetbrains.betterzheng.src.main.filehandling
package Main

import Filehandling.readfile

@main def helloWorld: Unit =
  println("Hello world!")
  val testvar: String = readfile;
  println(testvar)
  println(msg)

def msg = "I was compiled by Scala 3. :)"