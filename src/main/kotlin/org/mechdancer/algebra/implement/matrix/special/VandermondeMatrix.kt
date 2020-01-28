package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.checkElementsEquals
import org.mechdancer.algebra.function.matrix.checkSameSize
import org.mechdancer.algebra.function.matrix.traceValue
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
	private val elements: List<Double>
) : Matrix {

	override val column get() =  elements.size
	override fun get(r: Int, c: Int) = elements[c].pow(r)

	override val rows get() = List(row, ::row)
	override val columns get() = List(column, ::column)

	override fun row(r: Int) =
		List(column) { c -> get(r, c) }.toListVector()

	override fun column(c: Int) =
		List(row) { r -> get(r, c) }.toListVector()

	override val rank by lazy { min(row, elements.toSet().size) }

	override val det by lazy {
		when {
			row != column -> null
			row != rank   -> .0
			row == 1      -> elements.first()
			else          -> elements.indices.reversed().fold(1.0) { acc, i ->
				acc * (i - 1 downTo 0).fold(1.0) { sum, j ->
					sum * (elements[i] - (elements.getOrNull(j) ?: .0))
				}
			}
		}
	}

	override val trace by lazy { traceValue() }

	override fun equals(other: Any?) =
		this === other
			|| (other is Matrix
			&& checkSameSize(this, other)
			&& checkElementsEquals(this, other))

	override fun hashCode() = hash(row, elements)
	override fun toString() = matrixView("$row x $column Vandermonde matrix")

	companion object {
		operator fun get(row: Int, elements: Iterable<Number>) =
			elements
				.map(Number::toDouble)
				.let {
					if (it.all { x -> x == .0 }) ZeroMatrix[row, it.size]
					else VandermondeMatrix(row, it)
				}
	}
}
