package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.geometry.angle.Radian.Companion.halfPI
import kotlin.math.PI
import kotlin.math.abs

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

fun Vector2D.isZeroAngle() = toRad().isZero()
fun Vector2D.isAcute() = toRad().isAcute()
fun Vector2D.isRight() = toRad().isRight()
fun Vector2D.isObtuse() = toRad().isObtuse()
fun Vector2D.isFlat() = toRad().isFlat()
fun Vector2D.isRound() = toRad().isRound()

operator fun Degree.compareTo(other: Degree) = value.compareTo(other.value)
operator fun Degree.compareTo(other: Radian) = value.compareTo(other.value / PI * 180)

operator fun Radian.compareTo(other: Degree) = value.compareTo(other.value / 180 * PI)
operator fun Radian.compareTo(other: Radian) = value.compareTo(other.value)
