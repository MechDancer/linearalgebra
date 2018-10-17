package org.mechdancer.geometry.position

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.tie
import org.mechdancer.algebra.function.matrix.inverse
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.implement.vector.Vector2D

/**
 * 空间变换
 * 二维笛卡尔坐标系之间的变换
 * @param matrix 变换矩阵
 * @param vector 平移向量
 */
class Transformation(
	private val matrix: Matrix,
	private val vector: Vector
) {
	private val inverse by lazy { matrix.inverse()!! }

	/**
	 * 变换一点
	 */
	operator fun invoke(point: Vector2D) = matrix * point + vector

	/**
	 * 反变换一点
	 */
	fun reverse(point: Vector2D) = inverse * (point - vector)

	/**
	 * 判断变换是不是手性的
	 * 左手系到右手系的变换或右手系到左手系的变换定义为“手性的”
	 */
	fun isChiral() = matrix.det < 0

	override fun toString() = tie(matrix, vector)
}
