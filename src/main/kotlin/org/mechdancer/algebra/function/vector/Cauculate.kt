package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.vector.ListVector
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.listVectorOfZero
import org.mechdancer.algebra.uniqueValue
import kotlin.math.abs

operator fun Vector.times(k: Number) = toList().map { it * k.toDouble() }.let(::ListVector)
operator fun Vector.div(k: Number) = toList().map { it / k.toDouble() }.let(::ListVector)

operator fun Vector2D.times(k: Number) = k.toDouble().let { Vector2D(x * it, y * it) }
operator fun Vector2D.div(k: Number) = k.toDouble().let { Vector2D(x / it, y / it) }

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

operator fun Vector2D.plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
operator fun Vector2D.minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
operator fun Vector2D.times(other: Vector2D) = Vector2D(x * other.x, y * other.y)
operator fun Vector2D.div(other: Vector2D) = Vector2D(x / other.x, y / other.y)
infix fun Vector2D.dot(other: Vector2D) = x * other.x + y * other.y

operator fun Vector.unaryPlus() = this
operator fun Vector.unaryMinus() = toList().map { -it }.let(::ListVector)
operator fun Vector2D.unaryMinus() = Vector2D(-x, -y)

fun Vector.reversed() = -this
fun Vector.normalize() = div(length)

/**
 * 求向量表示的点集的“重心”
 */
fun Collection<Vector>.centre() =
	uniqueValue(Vector::dim)
		?.let(::listVectorOfZero)
		?.let { fold(it) { sum, v -> sum + v } }
		?.div(size)
		?: throw UnsupportedOperationException("vector dimensions are different")

/**
 * 范数
 * @param n 阶数
 */
fun Vector.norm(n: Int = 2) =
	when (n) {
		-1   -> toList().map(::abs).max()
		1    -> toList().sumByDouble(::abs)
		2    -> length
		else -> throw UnsupportedOperationException("please invoke length(-1) for infinite length")
	} ?: .0
