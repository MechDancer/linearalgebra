package org.mechdancer.geometry.angle

import kotlin.math.PI

inline class Angle(val rad: Double) : Comparable<Angle> {
    val degree get() = rad / PI * 180

    @Deprecated("",
                replaceWith = ReplaceWith("rad"))
    val value
        get() = rad

    @Deprecated("",
                replaceWith = ReplaceWith("rad"))
    fun asRadian() = rad

    @Deprecated("",
                replaceWith = ReplaceWith("degree"))
    fun asDegree() = degree

    override fun toString() = radianView()

    override fun compareTo(other: Angle) =
        rad.compareTo(other.rad)

    companion object {
        const val halfPI = PI / 2
        const val twicePI = PI * 2
    }
}
