package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.component1
import org.mechdancer.algebra.function.vector.component2
import org.mechdancer.algebra.function.vector.dot
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.builder.foldToRows
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import org.mechdancer.algebra.implement.matrix.special.HilbertMatrix
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.Vector3D
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * 雅可比方法求实对称矩阵特征值
 * @receiver 不是对称矩阵将导致异常
 * @param    epsilon 阈值，绝对值小于此阈值的非对角元素不再变换
 * @return   特征值特征向量对的集合
 */
fun Matrix.eigen(epsilon: Double = 1e-8): List<Pair<Double, Vector>> {
    val (q, sigma) = evd(epsilon) ?: throw UnsupportedOperationException("must be symmetric")
    return sigma.diagonal
        .mapIndexed { i, value -> value to q.column(i) }
        .sortedByDescending { it.first }
}

fun Matrix.evd(epsilon: Double = 1e-8): Pair<Matrix, DiagonalMatrix>? {
    // 判断对称性
    if (isNotSymmetric()) return null
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
        val pq = data[p, q].takeIf { abs(it) > epsilon } ?: break
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

    return eigenVectors.foldToRows(dim) to DiagonalMatrix((0 until dim).map { data[it * it] })
}

fun Matrix.svd(epsilon: Double = 1e-8): Triple<Matrix, Matrix, Matrix> {
    // 检查，如果对称退化到特征值分解
    val eigen = evd(epsilon)
    if (eigen != null) {
        val (q, sigma) = eigen
        return Triple(q, sigma, q)
    }
    val t = transpose()
    val (u, sigmaU) = (this * t).evd(epsilon)!!
    val (v, sigmaV) = (t * this).evd(epsilon)!!
    println(this * t)
    println(t * this)
    println(sigmaU)
    println(sigmaV)
    return Triple(u, sigmaU, v)
}

fun main() {
    HilbertMatrix[4].eigen().forEach(::println)
//    matrix {
//        row(102, 105, 114)
//        row(105, 137, 110)
//        row(114, 110, 123)
//    }.evd()?.let(::println)
//    val matrix = matrix {
//        row(1, 5, 7, 6, 1)
//        row(2, 1, 10, 4, 4)
//        row(3, 6, 7, 5, 2)
//    }
//    val (u, sigma, v) = matrix.svd()
//    println(u)
//    println(v)
}
