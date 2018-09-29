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
		data: List<Double>
	) : Matrix, List<Double> by data {
		override val row = size / column
		override fun get(r: Int, c: Int) = this[r * column + c]

		override fun row(r: Int) = drop(r * column).take(column).toListVector()
		override fun column(c: Int) = filterIndexed { i, _ -> i % c == 0 }.toListVector()

		override val rows = (0 until row).map(::row)
		override val columns = (0 until column).map(::column)
	}
}
