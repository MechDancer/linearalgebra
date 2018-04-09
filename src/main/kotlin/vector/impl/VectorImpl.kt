package vector.impl

import vector.Vector
import kotlin.math.sqrt

open class VectorImpl(override val data: List<Double>) : Vector, Cloneable {

	override val dimension: Int
		get() = data.size


	private fun operate(other: Vector, operation: (v1: Double, v2: Double) -> Double): Vector {
		checkDimension(other)
		return data.indices.map { operation(data[it], other.data[it]) }.let(::VectorImpl)
	}


	private fun checkDimension(other: Vector) =
			if (this.dimension != other.dimension) throw IllegalArgumentException("维度不同,无法操作")
			else Unit

	override operator fun get(index: Int): Double = data[index]

	override operator fun plus(other: Vector): Vector = operate(other) { v1, v2 -> v1 + v2 }

	override operator fun minus(other: Vector): Vector = operate(other) { v1, v2 -> v1 - v2 }

	override operator fun div(k: Double): Vector = VectorImpl(data.map { it / k })

	override operator fun times(k: Double): Vector = VectorImpl(data.map { it * k })

	override infix fun dot(other: Vector): Double {
		checkDimension(other)
		return (0 until dimension).sumByDouble { data[it] * other[it] }
	}

	override operator fun unaryMinus(): Vector = VectorImpl(data.map { -it })

	override infix fun to(other: Vector): Vector = other - this

	override fun norm(): Double = sqrt(data.sumByDouble { it * it })

	override fun getOrElse(index: Int, defaultValue: (Int) -> Double) =
			data.getOrElse(index, defaultValue)


	override fun toString(): String = "${dimension}DVector(${data.joinToString(separator = ",")})"

	override fun equals(other: Any?): Boolean {
		if (other !is Vector) return false
		return other.data == data
	}

	override fun hashCode(): Int {
		return data.hashCode()
	}
}
