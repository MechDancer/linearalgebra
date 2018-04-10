package matrix.builder

import matrix.DefineType
import matrix.Matrix
import matrix.impl.MatrixImpl

class MatrixBuilderDsl internal constructor(private val defineType: DefineType) {

	private val cache = mutableListOf<List<Double>>()


	operator fun Row.get(vararg double: Double) {
		cache.add(double.toList())
	}

	internal fun build(): Matrix = MatrixImpl(defineType, cache)
}

fun matrix(defineType: DefineType = DefineType.ROW,
           block: MatrixBuilderDsl.() -> Unit) =
		MatrixBuilderDsl(defineType).apply(block).build()