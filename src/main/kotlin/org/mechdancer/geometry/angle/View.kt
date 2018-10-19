package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import java.text.DecimalFormat

private val formatter = DecimalFormat("#.##")
private fun format(num: Double): String = formatter.format(num)

fun Angle.degreeView() = "${format(value)}°"
fun Angle.radianView() = "${format(value)} rad"
fun Vector2D.angleView() = "[${toList().joinToString(" ") { format(it) }}]"
