package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.contentEquals
import org.mechdancer.algebra.core.SubVector
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import kotlin.math.sqrt

class VectorExcludeOne(
	override val origin: Vector,
	private val exclude: Int
) : SubVector {
	override val dim get() = origin.dim - 1

	override fun get(i: Int) =
		origin[if (i < exclude) i else i - 1]

	override val length
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
		this === other
			|| (other is Vector
			&& toList() contentEquals other.toList())

	override fun hashCode() = toList().hashCode()
	override fun toString() = columnView()
}
