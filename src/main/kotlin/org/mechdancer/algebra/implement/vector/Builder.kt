package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector

fun Iterable<Number>.toListVector() =
	map { it.toDouble() }.let(::ListVector)

fun Array<Number>.toListVector() =
	toList().toListVector()

fun listVectorOf(vararg item: Number) =
	item.map { it.toDouble() }.toListVector()

fun listVectorOfZero(dim: Int) =
	List(dim) { .0 }.toListVector()

fun Vector.without(i: Int) =
	VectorExcludeOne(this, i)
