package org.mechdancer.algebra.vector.impl

import org.mechdancer.algebra.dimensionStateError
import org.mechdancer.algebra.vector.Vector
import kotlin.math.sqrt

open class VectorImpl internal constructor(data: List<Double>)
	: Vector by VectorImplCore(data) {

	private class VectorImplCore(val data: List<Double>)
		: Vector, List<Double> by data {
		override val dimension = size

		//检查other是否与this维数相同
		private fun checkDimension(other: Vector) =
			takeUnless { dimension != other.dimension } ?: throw dimensionStateError

		//逐项执行某项操作
		private fun operate(other: Vector, operation: (v1: Double, v2: Double) -> Double): Vector {
			checkDimension(other)
			return VectorImpl(zip(other.toList(), operation))
		}

		override operator fun plus(other: Vector): Vector = operate(other) { v1, v2 -> v1 + v2 }

		override operator fun minus(other: Vector): Vector = operate(other) { v1, v2 -> v1 - v2 }

		override operator fun times(k: Double): Vector = VectorImpl(map { it * k })

		override infix fun dot(other: Vector): Double {
			checkDimension(other)
			return zip(other.toList()) { a, b -> a * b }.sum()
		}

		override val norm by lazy { sqrt(sumByDouble { it * it }) }

		override fun toList() = data

		override fun toSimpleString() = "${dimension}DVector(${joinToString(separator = ", ")})"
	}

	fun getOrElse(index: Int, default: Double) =
		if (index in 0 until dimension) this[index] else default

	override fun equals(other: Any?) =
		other is Vector && other.toList() == toList()

	override fun hashCode() = toList().hashCode()
}


