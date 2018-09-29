package org.mechdancer.delegateeverything.list.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.list.vector.toListVector

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

		override fun row(r: Int) = drop(r * column).take(column).toListVector()
		override fun column(c: Int) = filterIndexed { i, _ -> i % c == 0 }.toListVector()

		override val rows get() = (0 until row).map(::row)
		override val columns get() = (0 until column).map(::column)

		override fun equals(other: Any?) =
			when (other) {
				is ListMatrix ->
					column == other.column && data == other.data
				is Matrix     ->
					row == other.row &&
						column == other.column &&
						(0 until row).all { r ->
							(0 until column).all { c ->
								other[r, c] == this[r, c]
							}
						}
				else          -> false
			}

		override fun hashCode() = data.hashCode()

		override fun toString(): String = "$row * $column Matrix by List"
	}
}
