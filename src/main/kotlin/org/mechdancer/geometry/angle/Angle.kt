package org.mechdancer.geometry.angle

import org.mechdancer.algebra.doubleEquals
import kotlin.math.PI

data class Angle(val value: Double) : Comparable<Angle> {
    fun asRadian() = value
    fun asDegree() = value / PI * 180

    override fun toString() = radianView()

    override fun equals(other: Any?) =
        this === other
        || (other is Angle && doubleEquals(value, other.value))

    override fun hashCode() =
        super.hashCode()

    override fun compareTo(other: Angle) =
        value.compareTo(other.value)

    companion object {
        const val halfPI = PI / 2
        const val twicePI = PI * 2
    }
}
