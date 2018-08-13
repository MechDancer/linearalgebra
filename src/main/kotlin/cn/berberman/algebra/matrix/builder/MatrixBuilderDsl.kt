package cn.berberman.algebra.matrix.builder

import cn.berberman.algebra.matrix.Matrix
import cn.berberman.algebra.matrix.impl.MatrixImpl

class MatrixBuilderDsl internal constructor() {

	private val cache = mutableListOf<MutableList<Double>>()

	operator fun Row.get(vararg double: Double) {
		cache.add(double.toList().toMutableList())
	}

	operator fun Column.get(vararg double: Double) {
		if (double.size != cache.last().size) throw IllegalArgumentException("行数错误")
		double.forEachIndexed { index, d ->
			cache.last()[index] = d
		}
	}

	internal fun build(): Matrix = MatrixImpl(cache)
}

fun matrix(block: MatrixBuilderDsl.() -> Unit) =
		MatrixBuilderDsl().apply(block).build()
