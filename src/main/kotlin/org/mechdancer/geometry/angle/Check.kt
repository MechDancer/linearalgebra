package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.geometry.angle.Angle.Companion.halfPI
import org.mechdancer.geometry.angle.Angle.Companion.twicePI
import kotlin.math.PI
import kotlin.math.abs

fun Angle.isZero() = 0.0 == value
fun Angle.isAcute() = -halfPI < value && value < halfPI
fun Angle.isRight() = halfPI == abs(value)
fun Angle.isObtuse() = abs(value).let { halfPI < it && it < PI }
fun Angle.isFlat() = PI == abs(value)
fun Angle.isRound() = twicePI == abs(value)

fun Vector2D.isZeroAngle() = toAngle().isZero()
fun Vector2D.isAcute() = toAngle().isAcute()
fun Vector2D.isRight() = toAngle().isRight()
fun Vector2D.isObtuse() = toAngle().isObtuse()
fun Vector2D.isFlat() = toAngle().isFlat()
fun Vector2D.isRound() = toAngle().isRound()

operator fun Angle.compareTo(other: Angle) = value.compareTo(other.value)
