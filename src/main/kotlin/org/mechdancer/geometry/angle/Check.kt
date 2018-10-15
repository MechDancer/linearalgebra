package org.mechdancer.geometry.angle

import org.mechdancer.algebra.core.Vector
import org.mechdancer.geometry.angle.Radian.Companion.halfPI
import kotlin.math.PI
import kotlin.math.abs

fun Vector.is2D() = dim == 2
fun Vector.isNot2D() = dim != 2
fun Vector.isMoreThan2D() = dim > 2

fun Degree.isZero() = 0.0 == value
fun Degree.isAcute() = -90.0 < value && value < 90.0
fun Degree.isRight() = 90.0 == abs(value)
fun Degree.isObtuse() = abs(value).let { 90.0 < it && it < 180.0 }
fun Degree.isFlat() = 180.0 == abs(value)
fun Degree.isRound() = 360.0 == abs(value)

fun Radian.isZero() = 0.0 == value
fun Radian.isAcute() = -halfPI < value && value < halfPI
fun Radian.isRight() = halfPI == abs(value)
fun Radian.isObtuse() = abs(value).let { halfPI < it && it < PI }
fun Radian.isFlat() = PI == abs(value)
fun Radian.isRound() = 2 * PI == abs(value)

fun Vector.isZero() = toRad().isZero()
fun Vector.isAcute() = toRad().isAcute()
fun Vector.isRight() = toRad().isRight()
fun Vector.isObtuse() = toRad().isObtuse()
fun Vector.isFlat() = toRad().isFlat()
fun Vector.isRound() = toRad().isRound()
