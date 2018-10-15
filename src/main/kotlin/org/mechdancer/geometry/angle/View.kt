package org.mechdancer.geometry.angle

import org.mechdancer.algebra.core.Vector
import java.text.DecimalFormat

private val formatter = DecimalFormat("#.##")
private fun format(num: Double): String = formatter.format(num)

fun Degree.angleView() = "${format(value)}°"
fun Radian.angleView() = "${format(value)} rad"
fun Vector.angleView() = "[${toList().joinToString(" ") { format(it) }}]"
