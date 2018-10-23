package org.mechdancer.algebra.implement.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.matrix.determinantValue
import org.mechdancer.algebra.function.matrix.rankDestructive
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.toListVector

/**
 * 基于列表实现的矩阵
 */
class ListMatrix(
	override val column: Int,
	val data: List<Double>
) : Matrix {
	init {
		assert(data.size % column == 0)
	}

	override val row = data.size / column
	override fun get(r: Int, c: Int) = data[r * column + c]

	override fun row(r: Int) = rows[r]
	override fun column(c: Int) = columns[c]

	override val rows by lazy {
		(0 until row).map { r ->
			data.subList(r * column, (r + 1) * column).toListVector()
		}
	}
	override val columns by lazy {
		(0 until column).map { c ->
			data.filterIndexed { i, _ -> i % column == c }.toListVector()
		}
	}

	override val rank by lazy { toArrayMatrix().rankDestructive() }

	override val det by lazy { determinantValue() }

	override fun equals(other: Any?) =
		other is Matrix
			&& row == other.row
			&& column == other.column
			&& when (other) {
			is ListMatrix  -> data.zip(other.data, ::doubleEquals).all { it }
			is ArrayMatrix -> other.data.zip(data, ::doubleEquals).all { it }
			else           ->
				(0 until row).all { r ->
					(0 until column).all { c ->
						doubleEquals(other[r, c], this[r, c])
					}
				}
		}

	override fun hashCode() = data.hashCode()

	override fun toString() = matrixView("$row x $column Matrix")
}
