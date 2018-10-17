package org.mechdancer.geometry.position

import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.geometry.angle.Degree
import org.mechdancer.geometry.angle.Radian
import org.mechdancer.geometry.angle.rotate

fun Vector2D.rotate(angle: Number, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

fun Vector2D.rotate(angle: Degree, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

fun Vector2D.rotate(angle: Radian, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)
