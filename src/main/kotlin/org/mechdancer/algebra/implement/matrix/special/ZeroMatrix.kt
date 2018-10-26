package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.toSet
import org.mechdancer.algebra.hash
import org.mechdancer.algebra.implement.vector.listVectorOfZero
import org.mechdancer.algebra.uniqueValue

class ZeroMatrix
private constructor(
	override val row: Int,
	override val column: Int
) : Matrix {
	init {
		assert(row >= 0)
		assert(column >= 0)
	}

	override fun get(r: Int, c: Int) = .0

	override val rows get() = List(row) { listVectorOfZero(column) }
	override val columns get() = List(column) { listVectorOfZero(row) }

	override fun row(r: Int) = listVectorOfZero(column)
	override fun column(c: Int) = listVectorOfZero(row)

	override val rank = 0
	override val det = .0

	override fun equals(other: Any?) =
		other is Matrix
			&& row == other.row
			&& column == other.column
			&& (other is ZeroMatrix || other.toSet().uniqueValue() == .0)

	override fun hashCode() = hash(row, column)

	override fun toString() = matrixView()

	companion object {
		private val Order0 = ZeroMatrix(0, 0)
		private val Order1 = ZeroMatrix(1, 1)
		private val Order2 = ZeroMatrix(2, 2)
		private val Order3 = ZeroMatrix(3, 3)

		operator fun get(dim: Int) =
			when (dim) {
				0    -> ZeroMatrix.Order0
				1    -> ZeroMatrix.Order1
				2    -> ZeroMatrix.Order2
				3    -> ZeroMatrix.Order3
				else -> ZeroMatrix(dim, dim)
			}

		operator fun get(m: Int, n: Int) =
			if (m == n) get(m) else ZeroMatrix(m, n)
	}
}
