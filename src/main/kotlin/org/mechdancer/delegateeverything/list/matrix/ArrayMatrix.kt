package org.mechdancer.delegateeverything.list.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.ValueMutableMatrix
import org.mechdancer.delegateeverything.core.Vector
import org.mechdancer.delegateeverything.list.vector.toListVector

/**
 * [Matrix] of [DoubleArray]
 * 基于数组实现的矩阵
 * 值可变，线程不安全
 */
class ArrayMatrix(colume: Int, val array: DoubleArray)
	: ValueMutableMatrix by ArrayMatrixCore(colume, array) {
	init {
		assert(array.size % colume == 0)
	}

	private class ArrayMatrixCore(override val column: Int, val array: DoubleArray)
		: ValueMutableMatrix {
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

		override fun setRow(r: Int, value: Vector) {
			assert(value.dimension == column)
			(0 until column).forEach { c -> set(r, c, value[c]) }
		}

		override fun setColumn(c: Int, value: Vector) {
			assert(value.dimension == row)
			(0 until row).forEach { r -> set(r, c, value[r]) }
		}

		override fun timesRow(r: Int, k: Double) {
			(0 until column).forEach { c -> set(r, c, k * get(r, c)) }
		}

		override fun plusToRow(r0: Int, r1: Int) {
			(0 until column).forEach { c -> set(r1, c, get(r0, c) + get(r1, c)) }
		}

		override fun exchangeRow(r0: Int, r1: Int) {
			(0 until column).forEach { c ->
				val temp = get(r0, c)
				set(r0, c, get(r1, c))
				set(r1, c, temp)
			}
		}

		override fun timesColumn(c: Int, k: Double) {
			(0 until row).forEach { r -> set(r, c, k * this[r, c]) }
		}

		override fun plusToColumn(c0: Int, c1: Int) {
			(0 until row).forEach { r -> set(r, c1, get(r, c0) + get(r, c1)) }
		}

		override fun exchangeColumn(c0: Int, c1: Int) {
			(0 until row).forEach { r ->
				val temp = get(r, c0)
				set(r, c0, get(r, c1))
				set(r, c1, temp)
			}
		}

		override fun equals(other: Any?) =
			when (other) {
				is ListMatrix  ->
					column == other.column && array.toList() == other.data
				is ArrayMatrix ->
					column == other.column && array.contentEquals(other.array)
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

		override fun hashCode() = array.hashCode()

		override fun toString() = matrixView()
	}
}
