package org.mechdancer.geometry.angle

import kotlin.math.PI

data class Angle(val value: Double) {
	fun asRadian() = value
	fun asDegree() = value / PI * 180

	override fun toString() = radianView()

	companion object {
		const val halfPI = PI / 2
		const val twicePI = PI * 2
	}
}
