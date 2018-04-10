package matrix

import matrix.impl.MatrixImpl
import vector.Vector

interface Matrix {

	val dimension: Int

	val defineType: DefineType

	val data: List<List<Double>>

	val row: Int

	val column: Int

	val isSquare: Boolean

	operator fun get(row: Int, column: Int): Double

	operator fun plus(other: Matrix): Matrix

	operator fun minus(other: Matrix): Matrix

	operator fun times(k: Double): Matrix

	operator fun times(other: Matrix): Matrix

	operator fun times(vector: Vector): Vector

	operator fun invoke(other: Matrix): Matrix

	operator fun invoke(vector: Vector): Vector

	operator fun div(other: Matrix): Matrix

	operator fun div(k: Double): Matrix


	@Deprecated("没什么卵用")
	infix fun pow(n: Int): Matrix


	fun companion(): Matrix

	fun transpose(): Matrix

	fun inverse(): Matrix

	companion object {

		fun zeroOf(row: Int, column: Int): Matrix {
			if (row <= 0 || column <= 0) throw IllegalArgumentException("矩阵参数错误")
			return List(row) { List(column) { .0 } }.let(::MatrixImpl)
		}

		fun unitOf(dimension: Int): Matrix {
			if (dimension <= 0) throw IllegalArgumentException("矩阵参数错误")
			return List(dimension) { r ->
				List(dimension) { c ->
					if (c == r) 1.0 else .0
				}
			}.let(::MatrixImpl)
		}
	}
}
