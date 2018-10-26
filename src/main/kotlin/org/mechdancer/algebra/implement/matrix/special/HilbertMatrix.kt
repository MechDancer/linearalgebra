package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.hash
import kotlin.math.min

/**
 * 希尔伯特矩阵
 */
class HilbertMatrix
private constructor(
	override val row: Int,
	override val column: Int
) : Matrix {
	override fun get(r: Int, c: Int) =
		1.0 / (r + c + 1)

	override fun row(r: Int) = getRow(r)
	override fun column(c: Int) = getColumn(c)
	override val rows get() = getRows()
	override val columns get() = getColumns()

	override val rank get() = min(row, column)
	override val det by lazy { determinantValue() }

	override fun equals(other: Any?) =
		this === other
			|| (other is Matrix
			&& checkSameSize(this, other)
			&& (other is HilbertMatrix
			|| checkElementsEquals(this, other)))

	override fun hashCode() = hash(row, column)

	override fun toString() = matrixView("$row x $column Hilbert matrix")

	companion object {
		operator fun get(m: Int, n: Int) = HilbertMatrix(m, n)
		operator fun get(d: Int) = HilbertMatrix(d, d)
	}
}
