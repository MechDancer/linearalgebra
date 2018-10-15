package org.mechdancer.geometry.angle

import kotlin.math.PI

/**
 * Radian of Degree
 * 弧度
 */
class Radian(val value: Double) {
	override fun toString() = angleView()

	companion object {
		const val halfPI = PI / 2
		val zero = Radian(.0)
		val right = Radian(halfPI)
		val flat = Radian(PI)
		val round = Radian(PI * 2)
	}
}
