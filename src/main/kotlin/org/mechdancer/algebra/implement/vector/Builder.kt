package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector

// iterable -> vector

fun Iterable<Number>.toListVector() =
	map { it.toDouble() }.let(::ListVector)

// array -> vector

fun Array<Number>.toListVector() =
	map { it.toDouble() }.let(::ListVector)

// dsl builder

fun listVectorOf(vararg item: Number) =
	item.map { it.toDouble() }.toListVector()

fun listVectorOfZero(dim: Int) =
	List(dim) { .0 }.toListVector()

// to sub-vector

infix fun Vector.without(i: Int) =
	VectorExcludeOne(this, i)
