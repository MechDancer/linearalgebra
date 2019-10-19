package org.mechdancer.geometry.angle

import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.algebra.implement.vector.to3D
import org.mechdancer.geometry.angle.Angle.Companion.halfPI
import org.mechdancer.geometry.angle.Angle.Companion.twicePI
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

fun Vector3D.rotateX(angle: Angle) =
    (angle.asRadian().let { a ->
        matrix {
            row(cos(a), -sin(a), 0)
            row(sin(a), cos(a), 0)
            row(1, 0, 0)
        }
    } * this).to3D()

fun Vector3D.rotateY(angle: Angle) =
    (angle.asRadian().let { a ->
        matrix {
            row(cos(a), 0, -sin(a))
            row(0, 1, 0)
            row(sin(a), 0, cos(a))
        }
    } * this).to3D()

fun Vector3D.rotateZ(angle: Angle) =
    (angle.asRadian().let { a ->
        matrix {
            row(1, 0, 0)
            row(0, cos(a), -sin(a))
            row(0, sin(a), cos(a))
        }
    } * this).to3D()


fun Vector3D.rotateXYZ(rx: Angle, ry: Angle, rz: Angle) =
    (rx.asRadian().let { x ->
        ry.asRadian().let { y ->
            rz.asRadian().let { z ->
                matrix {
                    row(1, 0, 0)
                    row(0, cos(z), -sin(z))
                    row(0, sin(z), cos(z))
                } * matrix {
                    row(cos(y), 0, -sin(y))
                    row(0, 1, 0)
                    row(sin(y), 0, cos(y))
                } * matrix {
                    row(cos(x), -sin(x), 0)
                    row(sin(x), cos(x), 0)
                    row(1, 0, 0)
                }
            }
        }
    } * this).to3D()


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
