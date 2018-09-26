package org.mechdancer.algebra.matrix.builder

import org.mechdancer.algebra.dimensionStateError
import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.matrix.impl.MatrixImpl

class MatrixBuilderDsl internal constructor() {

	private val cache = mutableListOf<MutableList<Double>>()

	operator fun Row.get(vararg double: Double) {
		cache.add(double.toList().toMutableList())
	}

	operator fun Column.get(vararg double: Double) {
		if (double.size != cache.last().size) dimensionStateError()
		double.forEachIndexed { index, d ->
			cache.last()[index] = d
		}
	}

	internal fun build(): Matrix = MatrixImpl(cache)
}

fun matrix(block: MatrixBuilderDsl.() -> Unit) =
		MatrixBuilderDsl().apply(block).build()

object Column
object Row