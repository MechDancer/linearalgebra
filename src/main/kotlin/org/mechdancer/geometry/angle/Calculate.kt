package org.mechdancer.geometry.angle

import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.geometry.angle.Angle.Companion.halfPI
import org.mechdancer.geometry.angle.Angle.Companion.twicePI
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

/**
 * Adjust radian into [-PI, +PI]
 * 调整弧度到[-PI, +PI]
 */
fun Angle.adjust(): Angle {
    var temp = value
    while (temp > +PI) temp -= twicePI
    while (temp < -PI) temp += twicePI
    return Angle(temp)
}

infix fun Angle.rotate(angle: Angle) =
    Angle(value + angle.value)

operator fun Angle.unaryMinus() =
    Angle(-value)

operator fun Angle.times(k: Number) =
    Angle(value * k.toDouble())

infix fun Vector2D.rotate(angle: Angle) =
    toAngle().rotate(angle).toVectorOf(length)

fun Vector2D.rotate(angle: Angle, centre: Vector2D) =
    minus(centre).rotate(angle).plus(centre)

fun Angle.complementary(): Angle {
    val abs = abs(value)
    assert(abs in .0..halfPI)
    return Angle(value.sign * (halfPI - abs))
}

fun Angle.supplementary(): Angle {
    val abs = abs(value)
    assert(abs in .0..PI)
    return Angle(value.sign * (PI - abs))
}

fun Vector2D.complementary() =
    toAngle().complementary().toVectorOf(length)

fun Vector2D.supplementary() =
    toAngle().supplementary().toVectorOf(length)
