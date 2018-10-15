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
		val zero by lazy { Radian(.0) }
		val right by lazy { Radian(PI / 2) }
		val flat by lazy { Radian(PI) }
		val round by lazy { Radian(PI * 2) }
	}
}
