package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.function.matrix.transpose
import org.mechdancer.algebra.function.matrix.transposeAssign
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.BuilderMode.Immutable
import org.mechdancer.algebra.implement.matrix.builder.BuilderMode.ValueMutable
import kotlin.math.abs

class MatrixBuilder {
	private var size = 0
	private var count = 0
	private val data = mutableListOf<Double>()

	fun row(vararg number: Number) {
		when {
			count < 0 -> throw RuntimeException()
			count > 0 -> if (size != number.size) throw RuntimeException()
			else      -> size = number.size
		}
		++count
		data.addAll(number.map { it.toDouble() })
	}

	fun column(vararg number: Number) {
		when {
			count > 0 -> throw RuntimeException()
			count < 0 -> if (size != number.size) throw RuntimeException()
			else      -> size = number.size
		}
		--count
		data.addAll(number.map { it.toDouble() })
	}

	internal fun build(mode: BuilderMode): Matrix =
		when (mode) {
			Immutable    ->
				ListMatrix(data.size / abs(count), data)
					.run { if (count < 0) transpose() else this }
			ValueMutable ->
				ArrayMatrix(data.size / abs(count), data.toDoubleArray())
					.apply { if (count < 0) transposeAssign() }
		}
}
