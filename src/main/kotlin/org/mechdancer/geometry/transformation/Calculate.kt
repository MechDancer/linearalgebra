package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.function.vector.DistanceType
import org.mechdancer.algebra.implement.equation.builder.equations
import org.mechdancer.algebra.implement.matrix.builder.foldToRows
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.algebra.implement.vector.vector2DOf

/**
 * 最小二乘法从点对集推算空间变换子
 * 任意维无约束
 */
fun PointMap.toTransformation(): Transformation? {
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

/**
 * 最小二乘法从点对集推算空间变换子
 * 二维直角非手性
 */
fun Point2DMap.toTransformation2D() =
	takeIf { size >= 2 }
		?.flatMap { point ->
			val (r, a) = point
			equations {
				this[+a.x, -a.y, 1, 0] = r.x
				this[+a.y, +a.x, 0, 1] = r.y
			}
		}
		?.toSet()
		?.solve()
		?.let {
			Transformation(
				matrix {
					row(+it[0], -it[1])
					row(+it[1], +it[0])
				},
				vector2DOf(it[2], it[3])
			)
		}

/**
 * 最小二乘法从点对集推算空间变换子
 * 二维直角手性
 */
fun Point2DMap.toTransformation2DChiral() =
	takeIf { size >= 2 }
		?.flatMap { point ->
			val (r, a) = point
			equations {
				this[+a.x, +a.y, 1, 0] = r.x
				this[-a.y, +a.x, 0, 1] = r.y
			}
		}
		?.toSet()
		?.solve()
		?.let {
			Transformation(
				matrix {
					row(+it[0], +it[1])
					row(+it[1], -it[0])
				},
				vector2DOf(it[2], it[3])
			)
		}

/**
 * 求点对集在变换下基于某种距离计算的误差
 */
fun PointMap.errorBy(transformation: Transformation, type: DistanceType) =
	mapValues { transformation(it.value) }
		.toList()
		.sumByDouble(type::between)
		.div(size)

