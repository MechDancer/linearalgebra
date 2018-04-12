package matrix.builder

import matrix.Matrix
import matrix.MatrixElement
import matrix.impl.MatrixImpl

class MatrixBuilderDsl internal constructor() {

	private val cache = mutableListOf<MatrixElement>()


	operator fun Row.get(vararg double: Double) {
		cache.add(double.toList())
	}

	internal fun build(): Matrix = MatrixImpl(cache)
}

fun matrix(block: MatrixBuilderDsl.() -> Unit) =
		MatrixBuilderDsl().apply(block).build()