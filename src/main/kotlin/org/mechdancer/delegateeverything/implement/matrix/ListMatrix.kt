package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.matrixView
import org.mechdancer.delegateeverything.implement.vector.toListVector

/**
 * [Matrix] delegated by [List] of [Double]
 * 用列表代理实现的矩阵
 */
class ListMatrix(
	column: Int,
	val data: List<Double>
) : Matrix by ListMatrixCore(column, data) {
	init {
		assert(data.size % column == 0)
	}

	private class ListMatrixCore(
		override val column: Int,
		val data: List<Double>
	) : Matrix, List<Double> by data {

		override val row = size / column
		override fun get(r: Int, c: Int) = this[r * column + c]

		override fun row(r: Int) = rows[r]
		override fun column(c: Int) = columns[c]

		override val rows by lazy {
			(0 until row).map { r ->
				subList(r * column, (r + 1) * column).toListVector()
			}
		}
		override val columns by lazy {
			(0 until column).map { c ->
				filterIndexed { i, _ -> i % column == c }.toListVector()
			}
		}

		override fun equals(other: Any?) =
			when (other) {
				is ListMatrix  ->
					column == other.column && data == other.data
				is ArrayMatrix ->
					column == other.column && data == other.array.toList()
				is Matrix      ->
					row == other.row &&
						column == other.column &&
						(0 until row).all { r ->
							(0 until column).all { c ->
								other[r, c] == this[r, c]
							}
						}
				else           -> false
			}

		override fun hashCode() = data.hashCode()

		override fun toString() = matrixView()
	}
}
