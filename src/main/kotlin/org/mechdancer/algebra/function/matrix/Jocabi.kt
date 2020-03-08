package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.core.rowView
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.special.HilbertMatrix
import kotlin.math.*
import kotlin.system.measureTimeMillis

/**
 * 构造平面旋转矩阵
 * @param order 矩阵阶数
 * @param theta 旋转弧度值
 * @param i     张成旋转二维面的轴序号
 * @param j     张成旋转二维面的轴序号
 */
fun rotationOnPlane(order: Int, theta: Double, i: Int, j: Int): Matrix {

    require(order >= 2)
    require(i != j)
    require(i in 0 until order)
    require(j in 0 until order)

    return object : Matrix {

        val cos = cos(theta)
        val sin = sin(theta)

        val x = min(i, j)
        val y = max(i, j)

        override val row get() = order
        override val column get() = order
        override fun get(r: Int, c: Int) =
            if (r == c) {
                if (r == x || r == y) cos else 1.0
            } else when {
                r == x && c == y -> -sin
                r == y && c == x -> +sin
                else             -> .0
            }

        override fun row(r: Int) = getRow(r)
        override fun column(c: Int) = getColumn(c)
        override val rows get() = getRows()
        override val columns get() = getColumns()

        override val rank = order
        override val det = 1.0
        override val trace = order - 2 + 2 * cos

        override fun equals(other: Any?) =
            other is Matrix
            && checkSameSize(this, other)
            && checkElementsEquals(this, other)

        override fun hashCode() =
            (order shl 12) or (i shl 8) or (j shl 4) or theta.hashCode()

        override fun toString() = matrixView()
    }
}

/**
 * 雅可比方法求实对称矩阵特征值
 * @receiver 不是对称矩阵将导致异常
 * @param    threshold 阈值，绝对值小于此阈值的非对角元素不再变换
 * @return   特征值特征向量对的集合
 */
fun Matrix.jacobiMethod(threshold: Double): List<Pair<Double, Vector>> {
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
        val rotateT = rotationOnPlane(dim, -theta, i, j)
        // 迭代
        middle = rotateT * middle * rotate
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
    min: Double = 1E-8
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
            .mapIndexed { r, c, v ->
                if (r != c && abs(v) > min) v * v else .0
            }
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
            val rotateT = rotationOnPlane(dim, -theta, i, j)
            // 迭代
            eigenvectors *= rotate

            middle = rotateT * middle * rotate
        }

        // 超时或满足要求退出
        if (t >= times || threshold == min)
            return when {
                threshold < max -> List(dim) { i -> middle[i, i] to eigenvectors.column(i) }
                else            -> null
            }?.sortedByDescending { it.first }
    }
}

private fun main() {
    val matrix = HilbertMatrix[36]

    matrix.jacobiMethod(1e-6).forEach { (a, b) -> println("$a\t${b.rowView()}") }

    while (true) {
        measureTimeMillis { matrix.jacobiMethod(1e-8) }.also(::println)
    }
}
