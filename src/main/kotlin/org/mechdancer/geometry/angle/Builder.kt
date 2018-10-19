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
fun Number.toRad() = Angle(toDouble())

/**
 * Point that a number is a degree of angle actually
 * 指示一个数字是角度
 */
fun Number.toDegree() = Angle(toDouble() / 180 * PI)

/**
 * Calculate the radian of a direction vector
 * 方向向量转弧度
 */
fun Vector2D.toAngle() = Angle(atan2(y, x))

/**
 * Calculate the direction vector of a radian
 * 弧度转方向向量
 */
fun Angle.toVector() = Vector2D(cos(value), sin(value))

/**
 * Calculate the direction vector of a radian
 * 弧度转方向向量
 */
fun Angle.toVectorOf(norm: Number) =
	norm.toDouble().let { Vector2D(it * cos(value), it * sin(value)) }
