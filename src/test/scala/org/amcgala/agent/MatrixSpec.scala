package org.amcgala.agent

import org.scalatest.{Matchers, FlatSpec}

/**
 * Matrix Specification Test
 */
class MatrixSpec extends FlatSpec with Matchers {


  "An empty 2x2 matrix of type Int" should " be created by calling Matrix(2,2)" in {
    val m = Matrix(2, 2)
    m(0, 0) should equal(0)
    m(0, 1) should equal(0)
    m(1, 0) should equal(0)
    m(1, 1) should equal(0)
  }

  it should "throw an Exception when accessing a wrong index" in {
    val m = Matrix(2, 2)
    evaluating {
      m(3, 3)
    } should produce[IllegalArgumentException]
  }

  it should "return the correct number of rows" in {
    Matrix(2, 2).rows should equal(2)
  }

  it should "return the correct number of columns" in {
    Matrix(2, 2).cols should equal(2)
  }

  it should "get its values from a data array" in {
    val m = Matrix(2, 2, 1, 2, 3, 4)
    m(0, 0) should equal(1)
    m(0, 1) should equal(2)
    m(1, 0) should equal(3)
    m(1, 1) should equal(4)
  }

  it should "support adding a second Matrix of the same size and return a new instance with the result" in {
    val m1 = Matrix(2, 2, 1, 2, 3, 4)
    val m2 = Matrix(2, 2, 1, 2, 3, 4)
    val res = m1 + m2
    res(0, 0) should equal(2)
    res(0, 1) should equal(4)
    res(1, 0) should equal(6)
    res(1, 1) should equal(8)
  }

  it should "support subtracting a second Matrix of the same size and return a new instance with the result" in {
    val m1 = Matrix(2, 2, 1, 2, 3, 4)
    val m2 = Matrix(2, 2, 1, 2, 3, 4)
    val res = m1 - m2
    res(0, 0) should equal(0)
    res(0, 1) should equal(0)
    res(1, 0) should equal(0)
    res(1, 1) should equal(0)
  }

  it should "multiply two matrices" in {
    val m1 = Matrix(2, 2, 1, 2, 3, 4)
    val m2 = Matrix(2, 2, 1, 0, 0, 1)
    val res = m1 * m2
    res(0, 0) should equal(1)
    res(0, 1) should equal(2)
    res(1, 0) should equal(3)
    res(1, 1) should equal(4)
  }

  it should "divide a matrix by a scalar" in {
    val m1 = Matrix(2, 2, 2, 4, 6, 8)
    val res = m1 / 2
    res(0, 0) should equal(1)
    res(0, 1) should equal(2)
    res(1, 0) should equal(3)
    res(1, 1) should equal(4)
  }


  it should "concatenate two matrices vertically" in {
    val m1 = Matrix(1, 2, 1, 2)
    val t = (Matrix(1, 2, 3, 3), 4)
    val concat = m1.:::(t)
    println(concat)
  }

  it should "compare two matrices element-wise to check for equality (true)" in {
    val m1 = Matrix(2, 2, 1, 2, 3, 4)
    val m2 = Matrix(2, 2, 1, 2, 3, 4)
    m1 should equal(m2)
  }

  it should "compare two matrices elementwise to check for equality (false)" in {
    val m1 = Matrix(2, 2, 1, 3, 3, 4)
    val m2 = Matrix(2, 2, 1, 2, 3, 4)
    m2 shouldNot equal(m2)
  }

  it should "add matrices with equal values to a Set only once" in {
    val set = Set(
      Matrix(2, 2, 1, 2, 3, 4),
      Matrix(2, 2, 1, 2, 3, 4)
    )

    set.size should equal(1)
  }

  it should "visualise the values of the Matrix" in {
    val m = Matrix(64, 64, ::)
    m.show()
  }


}
