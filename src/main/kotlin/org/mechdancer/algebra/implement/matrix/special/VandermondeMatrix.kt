package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.checkElementsEquals
import org.mechdancer.algebra.function.matrix.checkSameSize
import org.mechdancer.algebra.hash
import org.mechdancer.algebra.implement.vector.toListVector
import kotlin.math.min
import kotlin.math.pow

/**
 * 范德蒙矩阵
 */
class VandermondeMatrix
private constructor(
	override val row: Int,
	override val column: Int,
	private val elements: List<Double>
) : Matrix {
	override fun get(r: Int, c: Int) =
		elements[c].pow(r)

	override val rows get() = List(row, ::row)
	override val columns get() = List(column, ::column)

	override fun row(r: Int) =
		List(column) { c -> get(r, c) }.toListVector()

	override fun column(c: Int) =
		List(row) { r -> get(r, c) }.toListVector()

	override val rank get() = min(row, column)

	override val det: Double =
		if (elements.size >= 2) {
			elements.indices.reversed().fold(1.0) { acc, i ->
				acc * (i - 1 downTo 0).fold(1.0) { sum, j ->
					sum * (elements[i] - (elements.getOrNull(j) ?: .0))
				}
			}
		} else elements.firstOrNull() ?: .0

	override fun equals(other: Any?) =
		this === other
			|| (other is Matrix
			&& checkSameSize(this, other)
			&& checkElementsEquals(this, other))

	override fun hashCode() = hash(row, column, elements)
	override fun toString() = matrixView("$row x $column Vandermonde matrix")

	companion object {
		operator fun get(m: Int, n: Int, elements: Iterable<Number>) =
			elements
				.map(Number::toDouble)
				.let {
					if (it.all { x -> x == .0 }) ZeroMatrix[m, n]
					else VandermondeMatrix(m, n, it)
				}
	}
}
