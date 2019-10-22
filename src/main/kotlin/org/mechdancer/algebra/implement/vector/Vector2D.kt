package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.vector.x
import org.mechdancer.algebra.function.vector.y
import org.mechdancer.algebra.hash
import kotlin.math.hypot

data class Vector2D(val x: Double, val y: Double) : Vector {
    override val dim = 2

    override val length by lazy { hypot(x, y) }

    override fun get(i: Int) =
        when (i) {
            0    -> x
            1    -> y
            else -> throw IllegalArgumentException()
        }

    override fun toList() = listOf(x, y)

    override fun equals(other: Any?) =
        this === other
        || (other is Vector
            && other.dim == 2
            && doubleEquals(other.x, x)
            && doubleEquals(other.y, y))

    override fun hashCode() = hash(x, y)

    override fun toString() = columnView()
}
