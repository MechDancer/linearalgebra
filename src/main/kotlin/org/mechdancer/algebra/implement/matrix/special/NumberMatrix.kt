package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.checkSameSize
import org.mechdancer.algebra.function.matrix.filterIndexed
import org.mechdancer.algebra.implement.vector.toListVector
import kotlin.math.pow

/**
 * 不可变数值矩阵特化实现
 * @param row    行数
 * @param column 列数
 * @param value  数值
 */
class NumberMatrix
private constructor(
	override val row: Int,
	override val column: Int,
	val value: Double
) : Matrix {
	init {
		assert(row > 0)
		assert(column > 0)
	}

	// 是否有意义
	private val meaningful = row == column && value != .0

	override fun get(r: Int, c: Int) = if (r == c) value else .0

	override val rows get() = List(row) { r -> List(column) { c -> get(r, c) }.toListVector() }
	override val columns get() = List(column) { c -> List(row) { r -> get(r, c) }.toListVector() }

	override fun row(r: Int) = List(column) { c -> get(r, c) }.toListVector()
	override fun column(c: Int) = List(row) { r -> get(r, c) }.toListVector()

	override val rank = if (meaningful) row else 0
	override val det = if (meaningful) value.pow(row) else .0

	override fun equals(other: Any?) =
		other is Matrix
			&& checkSameSize(this, other)
			&& (
			(other as? NumberMatrix)?.value == value
				||
				other.filterIndexed { r, c, it ->
					it != if (r == c) value else .0
				}.isEmpty()
			)

	override fun hashCode() =
		row shl 8 or (column shl 4) or value.hashCode()

	override fun toString() = matrixView()

	companion object {
		private val UnitOrder1 = NumberMatrix(1, 1, 1.0)
		private val UnitOrder2 = NumberMatrix(2, 2, 1.0)
		private val UnitOrder3 = NumberMatrix(3, 3, 1.0)

		@JvmStatic
		operator fun get(dim: Int, value: Number) =
			value.toDouble()
				.let {
					when (it) {
						0.0  -> ZeroMatrix[dim]
						1.0  -> when (dim) {
							1    -> UnitOrder1
							2    -> UnitOrder2
							3    -> UnitOrder3
							else -> NumberMatrix(dim, dim, 1.0)
						}
						else -> NumberMatrix(dim, dim, it)
					}
				}

		@JvmStatic
		operator fun get(m: Int, n: Int, value: Number) =
			when {
				m == n      -> get(m, value)
				value == .0 -> ZeroMatrix[m, n]
				else        -> NumberMatrix(m, n, value.toDouble())
			}
	}
}
