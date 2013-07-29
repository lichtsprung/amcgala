package org.amcgala.agents

import org.ejml.simple.SimpleMatrix
import org.ejml.ops.{MatrixVisualization, MatrixFeatures}
import org.ejml.data.DenseMatrix64F
import java.util.Random

class Matrix {
  private var sm: SimpleMatrix = _

  private implicit def sm2m(simple: SimpleMatrix): Matrix = Matrix(simple)

  private implicit def sm2dm(simple: SimpleMatrix): DenseMatrix64F = simple.getMatrix

  def apply(row: Int, col: Int): Double = {
    sm.get(row, col)
  }

  def apply(row: Int, col: ::.type): Matrix = sm.extractVector(true, row)

  def apply(row: ::.type, col: Int): Matrix = sm.extractVector(false, col)

  def rows: Int = sm.numRows()

  def cols: Int = sm.numCols()

  def +(that: Matrix): Matrix = sm.plus(that.sm)

  def -(that: Matrix): Matrix = sm.minus(that.sm)

  def *(that: Matrix): Matrix = sm.mult(that.sm)

  def *(s: Double): Matrix = sm.scale(s)

  def /(s: Double): Matrix = sm.divide(s)

  def t: Matrix = sm.transpose()

  def i: Matrix = sm.invert()

  def :::(that: Matrix): Matrix = {
    if (cols != that.cols) throw new IllegalArgumentException("Number of columns must be the same for both matrices!")
    val concatList = sm.data.toList ++ that.sm.data.toList ++ Nil
    val rowCount = rows + that.rows
    val colCount = cols
    Matrix(rowCount, colCount, concatList: _*)
  }

  def :::(that: (Matrix, Int)): Matrix = {
    if (cols != that._1.cols) throw new IllegalArgumentException("Number of columns must be the same for both matrices!")
    val concatList = sm.data.toList ++ List.fill(that._2)(that._1.sm.data.toList).flatten
    val rowCount = rows + that._1.rows * that._2
    val colCount = cols
    Matrix(rowCount, colCount, concatList: _*)
  }


  def show() {
    MatrixVisualization.show(sm.getMatrix, "Matrix")
  }

  override def toString: String = sm.toString

  override def equals(obj: Any): Boolean = obj match {
    case m: Matrix => MatrixFeatures.isEquals(sm, m.sm)
    case _ => false
  }
}

object Matrix {
  def apply(rows: Int, cols: Int, data: Double*) = {
    val m = new Matrix()
    m.sm = new SimpleMatrix(rows, cols, true, data: _*)
    m
  }

  def apply(rows: Int, cols: Int) = {
    val m = new Matrix()
    m.sm = new SimpleMatrix(rows, cols)
    m
  }

  def apply(rows: Int, cols: Int, rand: ::.type) = {
    val m = new Matrix()
    m.sm = SimpleMatrix.random(rows, cols, 0.0, 1.0, new Random(System.currentTimeMillis))
    m
  }

  private[agents] def apply(sm: SimpleMatrix) = {
    val m = new Matrix()
    m.sm = sm
    m
  }
}
