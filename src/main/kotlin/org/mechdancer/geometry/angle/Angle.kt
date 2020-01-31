package org.mechdancer.geometry.angle

import kotlin.math.PI

inline class Angle(val value: Double) : Comparable<Angle> {
    fun asRadian() = value
    fun asDegree() = value / PI * 180

    override fun toString() = radianView()

    override fun compareTo(other: Angle) =
        value.compareTo(other.value)

    companion object {
        const val halfPI = PI / 2
        const val twicePI = PI * 2
    }
}
