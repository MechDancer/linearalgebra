package org.mechdancer.geometry.angle

import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.*
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.algebra.implement.vector.to3D
import org.mechdancer.geometry.angle.Angle.Companion.halfPI
import org.mechdancer.geometry.angle.Angle.Companion.twicePI
import org.mechdancer.geometry.angle3d.Angle3D
import kotlin.math.*

fun Double.adjustRight(max: Double, period: Double) =
    if (this > max)
        this - period * ((this - max) / period).toInt()
    else
        this

fun Double.adjustLeft(min: Double, period: Double) =
    if (this < min)
        this + period * ((min - this) / period).toInt() + period
    else
        this

fun Double.adjust(range: ClosedFloatingPointRange<Double>): Double {
    val period = range.endInclusive - range.start
    return this
        .adjustLeft(range.start, period)
        .adjustRight(range.endInclusive, period)
}

/**
 * Adjust radian into [-PI, +PI]
 * 调整弧度到[-PI, +PI]
 */
fun Angle.adjust() =
    Angle(rad.adjust(-PI..+PI))

infix fun Angle.rotate(angle: Angle) =
    Angle(rad + angle.rad)

operator fun Angle.unaryMinus() =
    Angle(-rad)

operator fun Angle.times(k: Number) =
    Angle(rad * k.toDouble())

infix fun Vector2D.rotate(angle: Angle) =
    toAngle().rotate(angle).toVectorOf(length)

fun Vector2D.rotate(angle: Angle, centre: Vector2D) =
    minus(centre).rotate(angle).plus(centre)

fun Vector3D.rotateX(angle: Angle) = (Angle3D.rx(angle) * this).to3D()

fun Vector3D.rotateY(angle: Angle) = (Angle3D.ry(angle) * this).to3D()

fun Vector3D.rotateZ(angle: Angle) = (Angle3D.rz(angle) * this).to3D()

fun Vector3D.rotate(angle3D: Angle3D) = (angle3D.matrix * this).to3D()

fun Vector3D.rotate(angle: Angle, axis: Vector3D) =
    angle.rad.let { theta ->
        this * cos(theta) + axis * (axis dot this) * (1 - cos(theta)) + (axis cross this) * sin(theta)
    }

/** 求余角 */
fun Angle.complementary(): Angle {
    val abs = abs(rad)
    assert(abs in .0..halfPI)
    return Angle(rad.sign * (halfPI - abs))
}

/** 求补角 */
fun Angle.supplementary(): Angle {
    val abs = abs(rad)
    assert(abs in .0..PI)
    return Angle(rad.sign * (PI - abs))
}
