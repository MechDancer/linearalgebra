package org.mechdancer.geometry.angle

/**
 * Degree of Degree
 * 角度
 */
data class Degree(val value: Double) {
	override fun toString() = angleView()

	companion object {
		val zero = Degree(.0)
		val right = Degree(90.0)
		val flat = Degree(180.0)
		val round = Degree(360.0)
	}
}
