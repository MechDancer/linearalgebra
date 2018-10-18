package org.mechdancer.geometry.position

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.implement.matrix.builder.foldToRows
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.geometry.angle.Degree
import org.mechdancer.geometry.angle.Radian
import org.mechdancer.geometry.angle.rotate

fun Vector2D.rotate(angle: Number, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

fun Vector2D.rotate(angle: Degree, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

fun Vector2D.rotate(angle: Radian, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

/**
 * 最小二乘法从任意维点对集推算空间变换子
 */
fun <T : Vector, U : Vector>
	Map<T, U>.toTransformation(): Transformation? {
	val dims =
		keys.map { it.dim }
			.toMutableSet()
			.apply { addAll(values.map { it.dim }) }

	assert(dims.size == 1)

	val dim = dims.first()
	val square = dim * dim
	val temp = DoubleArray(square + dim)

	return takeIf { size > dim }
		?.flatMap { point ->
			val args = point.value.toList()
			List(dim) { r ->
				temp.fill(.0)
				args.forEachIndexed { i, it -> temp[dim * r + i] = it }
				temp[square + r] = 1.0
				temp.toListVector() to point.key[r]
			}
		}
		?.toSet()
		?.solve()
		?.toList()
		?.let {
			Transformation(
				it.take(square) foldToRows dim,
				it.drop(square).toListVector()
			)
		}
}
