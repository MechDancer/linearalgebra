package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.DOUBLE_PRECISION
import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.component1
import org.mechdancer.algebra.function.vector.component2
import org.mechdancer.algebra.function.vector.dot
import org.mechdancer.algebra.implement.matrix.builder.*
import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.Vector3D
import kotlin.math.*

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

/** 特征值分解 */
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

    // 排序
    // val values = (0 until dim).asSequence()
    //     .map { i -> data[i, i] to i }
    //     .sortedByDescending { (value, _) -> value }
    return eigenVectors.foldToRows(dim) to DiagonalMatrix((0 until dim).map { data[it, it] })
}

/** 奇异值分解 */
fun Matrix.svd(epsilon: Double = 1e-8): Triple<Matrix, Matrix, Matrix> {
    // 检查，如果对称退化到特征值分解
    val eigen = evd(epsilon)
    if (eigen != null) {
        val (q, sigma) = eigen
        return Triple(q, sigma, q)
    }

    fun partEvd(a: Matrix, at: Matrix): Triple<Matrix, Matrix, Matrix> {
        val (m, square) = (a * at).evd(epsilon)!!
        val s = square.diagonal.map { if (it < DOUBLE_PRECISION) .0 else sqrt(it) }
        val w = listMatrixOf(row, column) { r, c -> if (r == c) s[r] else .0 }
        val others = (at * m * DiagonalMatrix(s.map { 1 / it }))
        return Triple(m, w, others)
    }

    return if (row > column)
        partEvd(this, transpose())
    else {
        val (v, w, u) = partEvd(transpose(), this)
        Triple(u, w, v)
    }
}
