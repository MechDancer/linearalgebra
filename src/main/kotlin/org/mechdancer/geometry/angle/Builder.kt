package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Point that a number is a radian actually
 * 指示一个数字是弧度
 */
fun Number.toRad() = Radian(toDouble())

/**
 * Point that a number is a degree of angle actually
 * 指示一个数字是角度
 */
fun Number.toDegree() = Degree(toDouble())

/**
 * Calculate the radian by its angle
 * 角度转弧度
 */
fun Degree.toRad() = Radian(value / 180 * PI)

/**
 * Calculate the angle by its radian
 * 弧度转角度
 */
fun Radian.toDegree() = Degree(value / PI * 180)

/**
 * Calculate the radian of a direction vector
 * 方向向量转弧度
 */
fun Vector2D.toRad(): Radian {
	assert(dim == 2)
	return Radian(atan2(y, x))
}

/**
 * Calculate the angle of a direction vector
 * 方向向量转角度
 */
fun Vector2D.toDegree(): Degree {
	assert(dim == 2)
	return Degree(atan2(y, x) / PI * 180)
}

/**
 * Calculate the direction vector of a radian
 * 弧度转方向向量
 */
fun Radian.toVector() =
	Vector2D(cos(value), sin(value))

/**
 * Calculate the direction vector of a angle
 * 弧度转方向向量
 */
fun Degree.toVector() =
	(value / 180 * PI).let { Vector2D(cos(it), sin(it)) }

/**
 * Calculate the direction vector of a radian
 * 弧度转方向向量
 */
fun Radian.toVectorOf(norm: Number) =
	norm.toDouble().let { Vector2D(it * cos(value), it * sin(value)) }

/**
 * Calculate the direction vector of a angle
 * 弧度转方向向量
 */
fun Degree.toListVectorOf(norm: Number) =
	toRad().toVectorOf(norm)
