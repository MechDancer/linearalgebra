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

fun Vector3D.rotateX(angle: Angle) = (Angle3D.rx(angle) * this).to3D()

fun Vector3D.rotateY(angle: Angle) = (Angle3D.ry(angle) * this).to3D()

fun Vector3D.rotateZ(angle: Angle) = (Angle3D.rz(angle) * this).to3D()

fun Vector3D.rotate(angle3D: Angle3D) = (angle3D.matrix * this).to3D()

fun Vector3D.rotate(angle: Angle, axis: Vector3D) =
    angle.asRadian().let { theta ->
        this * cos(theta) + axis * (axis dot this) * (1 - cos(theta)) + (axis cross this) * sin(theta)
    }

/** 求余角 */
fun Angle.complementary(): Angle {
    val abs = abs(value)
    assert(abs in .0..halfPI)
    return Angle(value.sign * (halfPI - abs))
}

/** 求补角 */
fun Angle.supplementary(): Angle {
    val abs = abs(value)
    assert(abs in .0..PI)
    return Angle(value.sign * (PI - abs))
}
