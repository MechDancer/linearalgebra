package org.mechdancer.geometry.angle

/**
 * Degree of Degree
 * 角度
 */
data class Degree(val value: Double) {
	override fun toString() = "$value°"

	companion object {
		val zero by lazy { Degree(.0) }
		val right by lazy { Degree(90.0) }
		val flat by lazy { Degree(180.0) }
		val round by lazy { Degree(360.0) }
	}
}
