package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.MutableMatrix
import org.mechdancer.delegateeverything.core.matrixView
import org.mechdancer.delegateeverything.implement.vector.toListVector

/**
 * 基于可变列表实现的矩阵
 */
class MutableListMatrix(column: Int, val list: MutableList<Double>)
	: MutableMatrix {
	init {
		assert(list.size % column == 0)
	}

	override var row = list.size / column
		private set
	override var column = column
		private set

	private fun index(r: Int, c: Int) = r * column + c
	override fun get(r: Int, c: Int) = list[index(r, c)]
	override operator fun set(r: Int, c: Int, value: Double) {
		list[index(r, c)] = value
	}

	override fun row(r: Int) = list.subList(r * column, (r + 1) * column).toListVector()
	override fun column(c: Int) = list.filterIndexed { i, _ -> i % column == c }.toListVector()

	override val rows get() = (0 until row).map(::row)
	override val columns get() = (0 until column).map(::column)

	override fun setRow(r: Int, vector: List<Double>) {
		assert(vector.size == column)
		vector.forEachIndexed { c, value -> set(r, c, value) }
	}

	override fun setColumn(c: Int, vector: List<Double>) {
		assert(vector.size == row)
		vector.forEachIndexed { r, value -> set(r, c, value) }
	}

	override fun timesRow(r: Int, k: Double) {
		if (k == 1.0) return
		for (i in index(r, 0) until index(r + 1, 0))
			list[i] *= k
	}

	override fun plusToRow(k: Double, r0: Int, r1: Int) {
		if (k == .0) return
		val difference = (r1 - r0) * column
		for (i in index(r0, 0) until index(r0 + 1, 0))
			list[i + difference] += k * list[i]
	}

	override fun exchangeRow(r0: Int, r1: Int) {
		if (r0 == r1) return
		(0 until column).forEach { c ->
			val temp = get(r0, c)
			set(r1, c, get(r1, c))
			set(r0, c, temp)
		}
	}

	override fun timesColumn(c: Int, k: Double) {
		if (k == 1.0) return
		var i = c
		for (r in 0 until row) {
			list[i] *= k
			i += column
		}
	}

	override fun plusToColumn(k: Double, c0: Int, c1: Int) {
		if (k == .0) return
		var i0 = c0
		var i1 = c1
		for (r in 0 until row) {
			list[i0] += k * list[i1]
			i0 += column
			i1 += column
		}
	}

	override fun exchangeColumn(c0: Int, c1: Int) {
		if (c0 == c1) return
		(0 until row).forEach { r ->
			val temp = get(r, c0)
			set(r, c0, get(r, c1))
			set(r, c1, temp)
		}
	}

	override fun addRow(r: Int, vector: List<Double>) {
		assert(vector.size == column)
		if (r == row) {
			list.addAll(vector)
		} else {
			val p = index(r + 1, 0)
			vector.forEach { list.add(p, it) }
		}
		++row
	}

	override fun addColumn(c: Int, vector: List<Double>) {
		assert(vector.size == row)
		var i = index(row - 1, c)
		vector
			.asReversed()
			.forEach {
				list.add(i, it)
				i -= column
			}
		++column
	}

	override fun removeRow(r: Int) {
		val p = index(r, 0)
		for (t in 0 until column)
			list.removeAt(p)
		--row
	}

	override fun removeColumn(c: Int) {
		var i = index(row - 1, c)
		for (t in 0 until row) {
			list.removeAt(i)
			i -= column
		}
		--column
	}

	override val rank get() = clone().getRankDestructive()

	override val det get() = det(this)

	override fun equals(other: Any?) =
		when (other) {
			is ListMatrix        ->
				column == other.column && list == other.list
			is MutableListMatrix ->
				column == other.column && list == other.list
			is ArrayMatrix       ->
				column == other.column && list == other.array.toList()
			is Matrix            ->
				row == other.row &&
					column == other.column &&
					(0 until row).all { r ->
						(0 until column).all { c ->
							other[r, c] == this[r, c]
						}
					}
			else                 -> false
		}

	override fun hashCode() = list.hashCode()

	override fun toString() = matrixView()

	override fun clone() = MutableListMatrix(column, list.toMutableList())
}
