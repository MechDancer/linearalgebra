package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.vector.ListVector

operator fun Vector.times(k: Number) = toList().map { it * k.toDouble() }.let(::ListVector)
operator fun Vector.div(k: Number) = toList().map { it / k.toDouble() }.let(::ListVector)

private fun differentDimException(a: Vector, b: Vector) =
	IllegalArgumentException("operate two vector of different dim (${a.dim} and ${b.dim})")

private fun Vector.zip(other: Vector, block: (Double, Double) -> Double) =
	takeIf { dim == other.dim }
		?.let { toList().zip(other.toList(), block) }
		?: throw differentDimException(this, other)

private fun Vector.zipToNew(other: Vector, block: (Double, Double) -> Double) =
	zip(other, block).let(::ListVector)

operator fun Vector.plus(other: Vector) = zipToNew(other) { a, b -> a + b }
operator fun Vector.minus(other: Vector) = zipToNew(other) { a, b -> a - b }
operator fun Vector.times(other: Vector) = zipToNew(other) { a, b -> a * b }
operator fun Vector.div(other: Vector) = zipToNew(other) { a, b -> a / b }
infix fun Vector.dot(other: Vector) = zip(other) { a, b -> a * b }.sum()

operator fun Vector.unaryPlus() = this
operator fun Vector.unaryMinus() = toList().map { -it }.let(::ListVector)

fun Vector.normalize() = div(norm)
