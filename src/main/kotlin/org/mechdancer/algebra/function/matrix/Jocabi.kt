package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.component1
import org.mechdancer.algebra.function.vector.component2
import org.mechdancer.algebra.function.vector.dot
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.ListVector
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.Vector3D
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * 雅可比方法求实对称矩阵特征值
 * @receiver 不是对称矩阵将导致异常
 * @param    threshold 阈值，绝对值小于此阈值的非对角元素不再变换
 * @return   特征值特征向量对的集合
 */
fun Matrix.jacobiMethod(threshold: Double = 1e-8): List<Pair<Double, Vector>> {
    // 判断对称性
    require(isSymmetric())
    val dim = row
    // 初始化特征值和特征向量
    val data = toArrayMatrix().data
    val eigenVectors = I[dim].toArrayMatrix().data

    operator fun DoubleArray.get(r: Int, c: Int) = get(r * dim + c)
    operator fun DoubleArray.set(r: Int, c: Int, n: Double) = set(r * dim + c, n)

    while (true) {
        // 找上三角中的最大值位置
        val (p, q) =
            sequence { for (c in 0 until dim) for (r in 0 until c) yield(r to c) }
                .maxBy { (r, c) -> abs(data[r, c]) }!!
        val pq = data[p, q].takeIf { abs(it) > threshold } ?: break
        val pp = data[p, p]
        val qq = data[q, q]
        val theta = .5 * atan(2 * pq / (pp - qq))
        val cos = cos(theta)
        val sin = sin(theta)
        val t = matrix {
            row(cos, sin)
            row(-sin, cos)
        }
        for (i in 0 until dim) { // 此处可并行
            // 特征值
            val (pi, qi) = t * Vector2D(data[p, i], data[q, i])
            data[p, i] = pi
            data[i, p] = pi
            data[q, i] = qi
            data[i, q] = qi
            // 特征向量
            val (ip, iq) = t * Vector2D(eigenVectors[i, p], eigenVectors[i, q])
            eigenVectors[i, p] = ip
            eigenVectors[i, q] = iq
        }
        val k = Vector3D(cos * cos, sin * sin, 2 * cos * sin)
        data[p, p] = k dot Vector3D(pp, qq, +pq)
        data[q, q] = k dot Vector3D(qq, pp, -pq)
        data[p, q] = .0
        data[q, p] = .0
    }

    return List(dim) { i -> data[i, i] to ListVector(List(dim) { eigenVectors[it, i] }) }
        .sortedByDescending { it.first }
}
