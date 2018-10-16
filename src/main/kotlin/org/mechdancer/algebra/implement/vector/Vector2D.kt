package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import org.mechdancer.algebra.function.vector.x
import org.mechdancer.algebra.function.vector.y
import kotlin.math.sqrt

class Vector2D(val x: Double, val y: Double) : Vector {
	override val dim = 2

	override fun get(i: Int) =
		when (i) {
			0    -> x
			1    -> y
			else -> throw IllegalArgumentException()
		}

	override val norm by lazy { sqrt(x * x + y * y) }

	override fun toList() = listOf(x, y)

	override fun equals(other: Any?) =
		other is Vector
			&& other.dim == 2
			&& other.x == x
			&& other.y == y

	override fun hashCode() =
		(x.hashCode() shl 4) + y.hashCode()

	override fun toString() = columnView()
}
