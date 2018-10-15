package org.mechdancer.geometry.angle

import java.text.DecimalFormat

/**
 * Degree of Degree
 * 角度
 */
data class Degree(val value: Double) {
	override fun toString() = angleView()

	companion object {
		private val formatter = DecimalFormat("#.##")
		fun format(num: Double): String = formatter.format(num)
		val zero by lazy { Degree(.0) }
		val right by lazy { Degree(90.0) }
		val flat by lazy { Degree(180.0) }
		val round by lazy { Degree(360.0) }
	}
}
