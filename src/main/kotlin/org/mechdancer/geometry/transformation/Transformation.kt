package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.dim
import org.mechdancer.algebra.function.matrix.inverse
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.select
import org.mechdancer.algebra.implement.matrix.Cofactor
import org.mechdancer.algebra.implement.matrix.builder.arrayMatrixOfUnit
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.toVector

/** 用齐次变换矩阵 [matrix] 存储的变换关系 */
class Transformation(val matrix: Matrix) {
    /** 变换的维数 */
    val dim = matrix
                  .dim
                  .takeIf { it > 0 }
                  ?.let { it - 1 }
              ?: throw IllegalArgumentException("transform matrix must be square")

    /** 对 [vector] 应用变换 */
    operator fun invoke(vector: Vector): Vector {
        require(dim == vector.dim) { "a ${dim}D Transformation cannot transform ${vector.dim}D vector" }
        return (matrix * (vector.toList() + 1.0).toListVector()).select(0 until dim)
    }

    /** 对 [vector] 应用变换中的线性成分 */
    fun invokeLinear(vector: Vector): Vector {
        require(dim == vector.dim) { "a ${dim}D Transformation cannot transform ${vector.dim}D vector" }
        return Cofactor(matrix, dim, dim) * vector
    }

    /** 串联另一个变换 [others] */
    operator fun times(others: Transformation) =
        Transformation(matrix * others.matrix)

    /** 求逆变换 */
    fun inverse() =
        Transformation(matrix.inverse())

    /**
     * 判断变换是不是手性的
     * 左手系到右手系的变换或右手系到左手系的变换定义为“手性的”
     */
    val isChiral by lazy { this.matrix.det ?: .0 < 0 }

    override fun toString() = matrix.matrixView()

    override fun equals(other: Any?) = other is Transformation
                                       && other.dim == dim
                                       && other.matrix == matrix

    override fun hashCode() = matrix.hashCode()

    companion object {
        /**
         * 生成 [dim] 维单位变换
         */
        fun unit(dim: Int) =
            Transformation(arrayMatrixOfUnit(dim + 1))

        /**
         * 非齐次形式转化为齐次形式
         */
        fun fromInhomogeneous(
            linear: Matrix,
            move: Vector
        ): Transformation {
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
            }.let(::Transformation)
        }

        /**
         * 将位姿转化为位姿上坐标系的变换
         */
        fun fromPose(
            p: Vector2D,
            d: Angle
        ) = d.toVector()
            .let {
                matrix {
                    row(+it.x, -it.y, +p.x)
                    row(+it.y, +it.x, +p.y)
                    row(0, 0, 1)
                }
            }
            .let(::Transformation)
    }
}
