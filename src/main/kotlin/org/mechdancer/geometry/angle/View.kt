package org.mechdancer.geometry.angle

import org.mechdancer.algebra.implement.vector.Vector2D
import java.text.DecimalFormat

private val formatter = DecimalFormat("#.##")

fun Angle.degreeView() = "${formatter.format(rad)}°"
fun Angle.radianView() = "${formatter.format(rad)}rad"
fun Vector2D.angleView() = "[${toList().joinToString(" ", transform = formatter::format)}]"
