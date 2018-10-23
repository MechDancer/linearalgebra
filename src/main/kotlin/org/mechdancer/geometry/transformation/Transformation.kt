package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.tie
import org.mechdancer.algebra.function.matrix.inverseOrNull
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.matrix.unaryMinus
import org.mechdancer.algebra.function.vector.plus

/**
 * 空间变换
 * 笛卡尔坐标系之间的变换
 * @param matrix 变换矩阵
 * @param vector 平移向量
 */
data class Transformation(
	val matrix: Matrix,
	val vector: Vector
) {
	/**
	 * 变换一点
	 */
	operator fun invoke(point: Vector) = matrix * point + vector

	/**
	 * 反变换一点
	 */
	val reversed by lazy {
		matrix.inverseOrNull()?.let {
			Transformation(it, -it * vector)
		}
	}

	/**
	 * 判断变换是不是手性的
	 * 左手系到右手系的变换或右手系到左手系的变换定义为“手性的”
	 */
	fun isChiral() = matrix.det < 0

	override fun toString() = tie(matrix, vector)
}
