package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.cofactorOf
import org.mechdancer.algebra.function.matrix.dim
import org.mechdancer.algebra.function.matrix.inverse
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.select
import org.mechdancer.algebra.implement.matrix.Cofactor
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.matrix.special.NumberMatrix
import org.mechdancer.algebra.implement.vector.toListVector

/** 用齐次变换矩阵 [matrix] 存储的变换关系 */
data class MatrixTransformation(val matrix: Matrix) : Transformation<MatrixTransformation> {

    override val dim: Int get() = matrix.dim - 1

    override fun times(p: Vector): Vector {
        require(dim == p.dim) { "a ${dim}D transformation cannot transform a ${p.dim}D vector" }
        return (matrix * (p.toList() + 1.0).toListVector()).select(0 until dim)
    }

    override fun times(tf: MatrixTransformation): MatrixTransformation =
        MatrixTransformation(matrix * tf.matrix)

    override fun inverse(): MatrixTransformation =
        MatrixTransformation(matrix.inverse())

    override fun equivalentWith(others: MatrixTransformation) =
        matrix == others.matrix

    init {
        require(dim > 0) { "transform matrix must be square" }
    }

    /** 对 [vector] 应用变换 */
    operator fun invoke(vector: Vector): Vector =
        this * vector

    /** 对 [vector] 应用变换中的线性成分 */
    fun invokeLinear(vector: Vector): Vector {
        require(dim == vector.dim) { "a ${dim}D Transformation cannot transform ${vector.dim}D vector" }
        return Cofactor(matrix, dim, dim) * vector
    }

    /**
     * 其次变换成分的行列式值，与一系列性质有关
     */
    val homogeneousDet by lazy { matrix.cofactorOf(dim, dim).det!! }

    /**
     * 判断变换是不是手性的
     * 左手系到右手系的变换或右手系到左手系的变换定义为“手性的”
     */
    fun isChiral() = homogeneousDet < 0

    companion object {
        /**
         * 生成 [dim] 维单位变换
         */
        fun unit(dim: Int) =
            MatrixTransformation(NumberMatrix[dim + 1, 1.0])

        /**
         * 非齐次形式转化为齐次形式
         */
        fun fromInhomogeneous(
            linear: Matrix,
            move: Vector
        ): MatrixTransformation {
            val dim = linear.dim
            require(dim == move.dim)
            return listMatrixOf(dim + 1, dim + 1) { r, c ->
                when (r) {
                    dim  -> when (c) {
                        dim  -> 1.0
                        else -> 0.0
                    }
                    else -> when (c) {
                        dim  -> move[r]
                        else -> linear[r, c]
                    }
                }
            }.let(::MatrixTransformation)
        }
    }
}
