package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.builder.arrayMatrixOfUnit
import kotlin.math.*

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

	// 初始化特征值和特征向量
	var middle = this
	var eigenvectors: Matrix = I[dim]

	while (true) {
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

	return List(dim) { i ->
		middle[i, i] to eigenvectors.column(i)
	}.sortedByDescending { it.first }
}

/**
 * 雅可比过关方法求实对称矩阵特征值
 * @receiver 不是对称矩阵将导致异常
 * @param min 阈值下限
 * @param max 阈值上限
 * @return   特征值特征向量对的集合
 */
fun Matrix.jacobiLevelUp(
	max: Double = 1E-3,
	min: Double = 1E-14
): List<Pair<Double, Vector>>? {
	assert(isSymmetric())
	assert(min < max)

	// 暂存元素信息
	data class EI(val r: Int, val c: Int, val value: Double) {
		val coordinate get() = r to c
	}

	// 每轮计算次数
	val times = dim * dim * 4

	// 初始化特征值和特征向量
	var middle = this
	var eigenvectors: Matrix = I[dim]

	// 关卡循环
	while (true) {
		// 统计计算次数
		var t = 1

		// 设计关卡阈值
		val threshold = middle
			.mapIndexed { r, c, v -> if (r != c) v * v else .0 }
			.sum()
			.let(::sqrt)
			.div(dim)
			.let { max(it, min) }

		// 计算循环
		while (t++ < times) {
			// 最大非对角线元素的序号
			val (i, j) = middle
				.mapIndexed { r, c, v -> EI(r, c, abs(v)) }
				.filter { it.r != it.c && it.value > threshold }
				.maxBy(EI::value)
				?.coordinate
				?: break

			// 旋转弧度
			val theta = .5 * atan(2 * middle[i, j] / (middle[i, i] - middle[j, j]))
			// 构造旋转矩阵
			val rotate = rotationOnPlane(dim, theta, i, j)
			// 迭代
			middle = rotate.transpose() * middle * rotate
			eigenvectors *= rotate

		}

		// 超时或满足要求退出
		if (t >= times || threshold == min)
			return when {
				threshold < max -> List(dim) { i -> middle[i, i] to eigenvectors.column(i) }
				else            -> null
			}?.sortedByDescending { it.first }
	}
}
