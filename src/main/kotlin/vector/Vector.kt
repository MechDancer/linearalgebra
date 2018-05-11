package vector

import matrix.MatrixElement
import vector.impl.Vector2D
import vector.impl.Vector3D

interface Vector {
	val dimension: Int

	val data: MatrixElement

	operator fun get(index: Int): Double

	operator fun plus(other: Vector): Vector

	operator fun minus(other: Vector): Vector

	operator fun div(k: Double): Vector

	operator fun times(k: Double): Vector

	infix fun dot(other: Vector): Double

	operator fun unaryMinus(): Vector

	infix fun to(other: Vector): Vector

	fun norm(): Double

	fun getOrElse(index: Int, defaultValue: (Int) -> Double): Double

	fun toSimpleString(): String

	companion object {


		fun zeroOf(dimension: Int): Vector =
				if (dimension <= 0) throw IllegalArgumentException("维度参数错误")
				else
					when (dimension) {
						2    -> Vector2D(.0, .0)
						3    -> Vector3D(.0, .0, .0)
						else -> DoubleArray(dimension).toList().toVector()
					}


		fun normalize(vector: Vector): Vector = vector / vector.norm()

	}

}
