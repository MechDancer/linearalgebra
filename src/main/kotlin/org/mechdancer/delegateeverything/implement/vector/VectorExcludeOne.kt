package org.mechdancer.delegateeverything.implement.vector

import org.mechdancer.delegateeverything.core.SubVector
import org.mechdancer.delegateeverything.core.Vector
import org.mechdancer.delegateeverything.core.columnView
import kotlin.math.sqrt

class VectorExcludeOne(
	override val origin: Vector,
	private val exclude: Int
) : SubVector {
	override val dim = origin.dim - 1

	override fun get(i: Int) =
		origin[if (i < exclude) i else i - 1]

	override val norm
		get() =
			(0..dim)
				.asSequence()
				.map { get(it) }
				.sumByDouble { it * it }
				.let(::sqrt)

	override fun toList() =
		origin.toList().toMutableList()
			.apply { removeAt(exclude) }

	override fun equals(other: Any?) =
		other is Vector && toList() == other.toList()

	override fun hashCode() = toList().hashCode()
	override fun toString() = columnView()
}
