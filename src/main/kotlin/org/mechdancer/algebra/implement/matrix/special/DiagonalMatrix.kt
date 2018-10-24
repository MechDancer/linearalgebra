package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.matrix.diagonal
import org.mechdancer.algebra.function.matrix.filterIndexed
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.algebra.uniqueValue

/**
 * 对角阵
 */
class DiagonalMatrix(
	val daigonal: List<Double>
) : Matrix {
	val dim = diagonal.size

	override val row get() = dim
	override val column get() = dim

	override fun get(r: Int, c: Int) =
		if (r == c) diagonal[r] else .0

	override val rows
		get() = List(dim) { r -> List(dim) { c -> get(r, c) }.toListVector() }
	override val columns
		get() = rows

	override fun row(r: Int) =
		List(dim) { c -> get(r, c) }.toListVector()

	override fun column(c: Int) = row(c)

	override val rank by lazy { daigonal.filter { it != .0 }.size }
	override val det by lazy {
		if (rank == diagonal.size)
			diagonal.fold(1.0) { r, i -> r * i }
		else .0
	}

	override fun equals(other: Any?) =
		other is Matrix
			&& row == other.row
			&& column == other.column
			&& (
			(other as? DiagonalMatrix)
				?.diagonal
				?.zip(other.diagonal, ::doubleEquals)
				?.all { it } == true
				||
				other.filterIndexed { r, c, it ->
					it != if (r == c) diagonal[r] else .0
				}.isEmpty()
			)

	override fun hashCode() = diagonal.hashCode()

	override fun toString() = matrixView("${diagonal.size}d diagonal matrix")

	companion object {
		operator fun get(elements: Iterable<Number>) =
			elements
				.map(Number::toDouble)
				.let { diagonal ->
					diagonal
						.uniqueValue()
						?.let { NumberMatrix[diagonal.size, it] }
						?: DiagonalMatrix(diagonal)
				}
	}
}
