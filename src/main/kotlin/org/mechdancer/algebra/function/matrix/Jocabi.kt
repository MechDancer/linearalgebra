package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.builder.arrayMatrixOfUnit
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * 构造平面旋转矩阵
 * @param order 矩阵阶数
 * @param theta 旋转弧度值
 * @param i     张成旋转二维面的轴序号
 * @param j     张成旋转二维面的轴序号
 */
fun rotationOnPlane(order: Int, theta: Double, i: Int, j: Int): Matrix {
	assert(order >= 2)
	assert(i != j)
	assert(i in 0 until order)
	assert(j in 0 until order)

	val result = arrayMatrixOfUnit(order)
	val cos = cos(theta)
	val sin = sin(theta)

	result[i, i] = cos
	result[j, j] = cos

	if (i < j) { //右上取-sin,左下取+sin
		result[i, j] = -sin
		result[j, i] = +sin
	} else {
		result[j, i] = -sin
		result[i, j] = +sin
	}
	return result
}

/**
 * 雅可比方法求实对称矩阵特征值
 * @receiver 不是对称矩阵将导致异常
 * @param    threshold 阈值，绝对值小于此阈值的非对角元素不再变换
 * @return   特征值特征向量对的集合
 */
infix fun Matrix.jacobiMethod(threshold: Double): List<Pair<Double, Vector>> {
	assert(isSymmetric())

	// 一维不必计算，此后每多一个维度就多迭代 10 倍次数
	val times = (1 until dim).fold(1) { r, _ -> r * 10 }
	// 初始化特征值和特征向量
	var middle = this
	var eigenvectors: Matrix = I[dim]

	for (t in 1..times) {
		// 最大非对角线元素的序号
		val (i, j) = middle
			.mapIndexed { r, c, v ->
				if (r != c) Triple(r, c, abs(v)) else null
			}
			.filterNotNull()
			.maxBy { it.third }
			?.takeIf { it.third > threshold }
			?.let { it.first to it.second }
			?: break

		// 旋转弧度
		val theta = .5 * atan(2 * middle[i, j] / (middle[i, i] - middle[j, j]))
		// 构造旋转矩阵
		val rotate = rotationOnPlane(dim, theta, i, j)
		// 迭代
		middle = rotate.transpose() * middle * rotate
		eigenvectors *= rotate
	}

	return List(dim) { i -> middle[i, i] to eigenvectors.column(i) }.sortedByDescending { it.first }
}
