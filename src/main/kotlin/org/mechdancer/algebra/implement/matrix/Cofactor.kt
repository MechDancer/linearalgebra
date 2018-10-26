package org.mechdancer.algebra.implement.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.SubMatrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.checkElementsEquals
import org.mechdancer.algebra.function.matrix.checkSameSize
import org.mechdancer.algebra.function.matrix.determinantValue
import org.mechdancer.algebra.function.matrix.rankDestructive
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.without

class Cofactor(
	override val origin: Matrix,
	private val pr: Int,
	private val pc: Int
) : SubMatrix {
	override val row get() = origin.row - 1
	override val column get() = origin.column - 1

	override fun get(r: Int, c: Int) =
		origin[if (r < pr) r else r + 1, if (c < pc) c else c + 1]

	override val rows
		get() = origin.rows.map { it.without(pr) }

	override val columns
		get() = origin.columns.map { it.without(pc) }

	override fun row(r: Int) =
		origin.row(r).without(pr)

	override fun column(c: Int) =
		origin.column(c).without(pc)

	override val rank = toArrayMatrix().rankDestructive()
	override val det = determinantValue()

	override fun equals(other: Any?) =
		this === other
			|| (other is Matrix
			&& checkSameSize(this, other)
			&& checkElementsEquals(this, other))

	override fun hashCode() = Triple(origin, pr, pc).hashCode()
	override fun toString() = matrixView("$row x $column Matrix")
}
