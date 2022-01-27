
import Filehandling.Calculator
import org.scalatest.funsuite.AnyFunSuite

class CalculatorTest extends AnyFunSuite {

  val calculator = new Calculator

  test("multiplication with 0 should always give 0") {
    assert(calculator.multiply(572389, 0) == 0)
    assert(calculator.multiply(-572389, 0) == 0)
    assert(calculator.multiply(0, 0) == 0)
  }

  test("dividing by 0 should throw a math error") {
    assertThrows[ArithmeticException](calculator.divide(57238, 0))
  }
}