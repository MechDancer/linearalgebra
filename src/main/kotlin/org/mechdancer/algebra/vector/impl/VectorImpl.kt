package org.mechdancer.algebra.vector.impl

import org.mechdancer.algebra.dimensionStateError
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.vector.Vector
import org.mechdancer.algebra.vector.toVector
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

open class VectorImpl internal constructor(final override val data: MatrixElement) : Vector, Cloneable {

	override val dimension: Int
		get() = data.size

	init {
		if (data.isEmpty()) throw IllegalArgumentException("vector data error")
	}

	private fun operate(other: Vector, operation: (v1: Double, v2: Double) -> Double): Vector {
		checkDimension(other)
		return data.indices.map { operation(data[it], other.data[it]) }.toVector()
	}

	private fun checkDimension(other: Vector) =
		if (this.dimension != other.dimension) dimensionStateError()
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

	override fun norm(): Double = sqrt(data.sumByDouble { it * it })

	override fun normalize(): Vector = this / norm()

	override fun includedAngle(other: Vector) = acos(dot(other) / norm() / other.norm())

	fun getOrElse(index: Int, defaultValue: (Int) -> Double) =
		data.getOrElse(index, defaultValue)

	override fun toString(): String = buildString {
		val maxDataLength = data.map { it.toString().length }.max()!!
		append(" ".repeat(maxDataLength / 2))
		appendln("${dimension}D Vector")
		data.forEachIndexed { index, d ->
			when (index) {
				0             -> append("┌")
				data.size - 1 -> append("└")
				else          -> append("│")
			}
			var dL = abs(maxDataLength - d.toString().length)
			val parity = dL % 2 == 0
			dL /= 2
			val right: Int = dL
			val left: Int = if (parity) dL else dL + 1
			append(" ".repeat(left))
			append(" $d ")
			append(" ".repeat(right))
			when (index) {
				0             -> append("┐")
				data.size - 1 -> append("┘")
				else          -> append("│")
			}
			appendln()
		}
	}

	override fun toSimpleString() = "${dimension}DVector(${data.joinToString(separator = ", ")})"
	override fun equals(other: Any?) = other is Vector && other.data == data
	override fun hashCode() = data.hashCode()
}
