package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.function.matrix.transpose
import org.mechdancer.algebra.function.matrix.transposeAssign
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.MatrixBuilder.Mode.Constant
import org.mechdancer.algebra.implement.matrix.builder.MatrixBuilder.Mode.ValueMutable

class MatrixBuilder {
	private var count = 0
	private val data = mutableListOf<Double>()
	private val column
		get() =
			if (count > 0) data.size / count
			else -count

	fun row(vararg number: Number) {
		if (count < 0) throw RuntimeException()
		++count
		data.addAll(number.map { it.toDouble() })
	}

	fun column(vararg number: Number) {
		if (count > 0) throw RuntimeException()
		--count
		data.addAll(number.map { it.toDouble() })
	}

	internal fun build(mode: Mode): Matrix =
		when (mode) {
			Constant     ->
				ListMatrix(column, data)
					.run { if (count < 0) transpose() else this }
			ValueMutable ->
				ArrayMatrix(column, data.toDoubleArray())
					.apply { if (count < 0) transposeAssign() }
		}

	enum class Mode {
		ValueMutable, Constant;
	}
}


