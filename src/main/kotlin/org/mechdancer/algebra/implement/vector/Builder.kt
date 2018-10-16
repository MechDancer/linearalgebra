package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.x
import org.mechdancer.algebra.function.vector.y

// vector -> vector

fun Vector.to2D(): Vector2D =
	this as? Vector2D
		?: takeIf { dim == 2 }?.let { Vector2D(x, y) }
		?: throw IllegalArgumentException()

fun Vector.toListVector(): ListVector =
	this as? ListVector ?: ListVector(toList())

// iterable -> vector

fun Iterable<Number>.toListVector() =
	map { it.toDouble() }.let(::ListVector)

// array -> vector

fun Array<Number>.toListVector() =
	map { it.toDouble() }.let(::ListVector)

// dsl builder

fun listVectorOf(vararg item: Number) =
	ListVector(item.map { it.toDouble() })

fun listVectorOfZero(dim: Int) =
	ListVector(List(dim) { .0 })

fun Vector2DOf(x: Number, y: Number) =
	Vector2D(x.toDouble(), y.toDouble())

fun Vector2DOfZero() =
	Vector2D(.0, .0)

// to sub-vector

infix fun Vector.without(i: Int) =
	VectorExcludeOne(this, i)
