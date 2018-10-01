package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.ValueMutableMatrix
import org.mechdancer.delegateeverything.core.matrixView
import org.mechdancer.delegateeverything.implement.vector.toListVector

/**
 * [Matrix] of [DoubleArray]
 * 基于数组实现的矩阵
 * 值可变，线程不安全
 */
class ArrayMatrix(override val column: Int, val array: DoubleArray)
	: ValueMutableMatrix {
	init {
		assert(array.size % column == 0)
	}

	override val row = array.size / column

	private fun index(r: Int, c: Int) = r * column + c
	override operator fun get(r: Int, c: Int) = array[index(r, c)]
	override operator fun set(r: Int, c: Int, value: Double) {
		array[index(r, c)] = value
	}

	override fun row(r: Int) = array.copyOfRange(r * column, (r + 1) * column).toList().toListVector()
	override fun column(c: Int) = array.filterIndexed { i, _ -> i % column == c }.toListVector()

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
			array[i] *= k
	}

	override fun plusToRow(k: Double, r0: Int, r1: Int) {
		if (k == .0) return
		val difference = (r1 - r0) * column
		for (i in index(r0, 0) until index(r0 + 1, 0))
			array[i + difference] += k * array[i]
	}

	override fun exchangeRow(r0: Int, r1: Int) {
		if (r0 == r1) return
		val temp = array.copyOfRange(index(r0, 0), index(r0 + 1, 0))
		System.arraycopy(array, index(r1, 0), array, index(r0, 0), column)
		System.arraycopy(temp, 0, array, index(r1, 0), column)
	}

	override fun timesColumn(c: Int, k: Double) {
		if (k == 1.0) return
		var i = c
		for (r in 0 until row) {
			array[i] *= k
			i += column
		}
	}

	override fun plusToColumn(k: Double, c0: Int, c1: Int) {
		if (k == .0) return
		var i0 = c0
		var i1 = c1
		for (r in 0 until row) {
			array[i0] += k * array[i1]
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

	override val rank get() = clone().getRankDestructive()

	override val det get() = det(this)

	override fun equals(other: Any?) =
		when (other) {
			is ListMatrix        ->
				column == other.column && array.toList() == other.list
			is ArrayMatrix       ->
				column == other.column && array.contentEquals(other.array)
			is Matrix            ->
				row == other.row &&
					column == other.column &&
					(0 until row).all { r ->
						(0 until column).all { c ->
							other[r, c] == get(r, c)
						}
					}
			else                 -> false
		}

	override fun hashCode() = array.hashCode()

	override fun toString() = matrixView()

	override fun clone() = ArrayMatrix(column, array)
}
