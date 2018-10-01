package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.SubMatrix
import org.mechdancer.delegateeverything.core.matrixView
import org.mechdancer.delegateeverything.implement.vector.without

class Cofactor(
	override val origin: Matrix,
	private val pr: Int,
	private val pc: Int
) : SubMatrix {
	override val row get() = origin.row - 1
	override val column get() = origin.column - 1

	override fun get(r: Int, c: Int) =
		origin[if (r < pr) r else r - 1, if (c < pc) c else c - 1]

	override val rows
		get() = origin.rows.map { it.without(pr) }

	override val columns
		get() = origin.columns.map { it.without(pc) }

	override fun row(r: Int) =
		origin.row(r).without(pr)

	override fun column(c: Int) =
		origin.column(c).without(pc)

	override val rank = toArrayMatrix().getRankDestructive()
	override val det = det(this)

	override fun equals(other: Any?) =
		other is Matrix &&
			row == other.row &&
			column == other.column &&
			(0 until row).all { r ->
				(0 until column).all { c ->
					other[r, c] == this[r, c]
				}
			}

	override fun hashCode() = Triple(origin, pr, pc).hashCode()
	override fun toString() = matrixView()
}
