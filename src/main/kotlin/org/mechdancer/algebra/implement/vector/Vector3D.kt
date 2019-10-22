package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.vector.x
import org.mechdancer.algebra.function.vector.y
import org.mechdancer.algebra.function.vector.z
import org.mechdancer.algebra.hash
import kotlin.math.sqrt

data class Vector3D(val x: Double, val y: Double, val z: Double) : Vector {
    override val dim: Int = 3

    override val length: Double by lazy { sqrt(x * x + y * y + z * z) }

    override fun get(i: Int): Double = when (i) {
        0    -> x
        1    -> y
        2    -> z
        else -> throw IllegalArgumentException()
    }

    override fun toList(): List<Double> = listOf(x, y, z)

    override fun equals(other: Any?): Boolean =
        this === other
        || (other is Vector
            && other.dim == 3
            && doubleEquals(other.x, x)
            && doubleEquals(other.y, y)
            && doubleEquals(other.z, z))

    override fun hashCode(): Int = hash(x, y, z)

    override fun toString(): String = columnView()
}
