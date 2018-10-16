package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.geometry.angle.Radian.Companion.halfPI
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sign

/**
 * Adjust radian into [-PI, +PI]
 * 调整弧度到[-PI, +PI]
 */
fun Radian.adjust(): Radian {
	var temp = value
	while (temp > +PI) temp -= 2 * PI
	while (temp < -PI) temp += 2 * PI
	return Radian(temp)
}

/**
 * Adjust angle into [-180°, +180°]
 * 调整角度到[-180°, +180°]
 */
fun Degree.adjust(): Degree {
	var temp = value
	while (temp > +180) temp -= 360
	while (temp < -180) temp += 360
	return Degree(temp)
}

infix fun Degree.rotate(other: Number) =
	Degree(value + other.toDouble())

infix fun Degree.rotate(other: Degree) =
	Degree(value + other.value)

infix fun Degree.rotate(other: Radian) =
	Degree(value + other.value / PI * 180)

infix fun Radian.rotate(other: Number) =
	Radian(value + other.toDouble())

infix fun Radian.rotate(other: Degree) =
	Radian(value + other.value / 180 * PI)

infix fun Radian.rotate(other: Radian) =
	Radian(value + other.value)

infix fun Vector2D.rotate(other: Number) =
	toRad().rotate(other).toVectorOf(norm)

infix fun Vector2D.rotate(other: Degree) =
	toRad().rotate(other).toVectorOf(norm)

infix fun Vector2D.rotate(other: Radian) =
	toRad().rotate(other).toVectorOf(norm)

fun Radian.complementary(): Radian {
	val abs = abs(value)
	assert(abs in .0..halfPI)
	return Radian(value.sign * (halfPI - abs))
}

fun Radian.supplementary(): Radian {
	val abs = abs(value)
	assert(abs in .0..PI)
	return Radian(value.sign * (PI - abs))
}

fun Degree.complementary(): Degree {
	val abs = abs(value)
	assert(abs in 0..90)
	return Degree(value.sign * (90 - abs))
}

fun Degree.supplementary(): Degree {
	val abs = abs(value)
	assert(abs in 0..180)
	return Degree(value.sign * (180 - abs))
}

fun Vector2D.complementary() =
	toRad().complementary().toVectorOf(norm)

fun Vector2D.supplementary() =
	toRad().supplementary().toVectorOf(norm)
