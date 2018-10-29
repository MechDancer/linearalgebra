package org.mechdancer.algebra.function.jama

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.ArrayMatrix

/**
 * 导入Jama
 */
fun Matrix.toJamaMatrix() =
	Array(row) { r -> DoubleArray(column) { c -> get(r, c) } }
		.let { Jama.Matrix(it, row, column) }

/**
 * 从Jama导入
 */
fun buildFromJama(m: Jama.Matrix) =
	ArrayMatrix(m.columnDimension, m.rowPackedCopy)
