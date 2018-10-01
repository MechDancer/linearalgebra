package org.mechdancer.algebra.implement.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.determinantValue
import org.mechdancer.algebra.function.matrix.rankDestructive
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.toListVector

/**
 * 基于列表实现的矩阵
 */
class ListMatrix(
	override val column: Int,
	val list: List<Double>
) : Matrix {
	init {
		assert(list.size % column == 0)
	}

	override val row = list.size / column
	override fun get(r: Int, c: Int) = list[r * column + c]

	override fun row(r: Int) = rows[r]
	override fun column(c: Int) = columns[c]

	override val rows by lazy {
		(0 until row).map { r ->
			list.subList(r * column, (r + 1) * column).toListVector()
		}
	}
	override val columns by lazy {
		(0 until column).map { c ->
			list.filterIndexed { i, _ -> i % column == c }.toListVector()
		}
	}

	override val rank by lazy { toArrayMatrix().rankDestructive() }

	override val det by lazy { determinantValue() }

	override fun equals(other: Any?) =
		when (other) {
			is ListMatrix  ->
				column == other.column && list == other.list
			is ArrayMatrix ->
				column == other.column && list == other.array.toList()
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

	override fun hashCode() = list.hashCode()

	override fun toString() = matrixView()
}
