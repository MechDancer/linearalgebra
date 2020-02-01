package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.geometry.angle.Angle.Companion.halfPI
import org.mechdancer.geometry.angle.Angle.Companion.twicePI
import kotlin.math.PI
import kotlin.math.abs

fun Angle.isZero() = 0.0 == rad
fun Angle.isAcute() = -halfPI < rad && rad < halfPI
fun Angle.isRight() = halfPI == abs(rad)
fun Angle.isObtuse() = abs(rad).let { halfPI < it && it < PI }
fun Angle.isFlat() = PI == abs(rad)
fun Angle.isRound() = twicePI == abs(rad)

fun Vector2D.isZeroAngle() = toAngle().isZero()
fun Vector2D.isAcute() = toAngle().isAcute()
fun Vector2D.isRight() = toAngle().isRight()
fun Vector2D.isObtuse() = toAngle().isObtuse()
fun Vector2D.isFlat() = toAngle().isFlat()
fun Vector2D.isRound() = toAngle().isRound()
