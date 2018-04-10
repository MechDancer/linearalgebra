package matrix

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

	@Deprecated("没什么卵用")
	infix fun pow(n: Int): Matrix


	fun companion(): Matrix

	fun transpose(): Matrix

	fun inverse(): Matrix


}
