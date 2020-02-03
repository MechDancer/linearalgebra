package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.DistanceType
import org.mechdancer.algebra.function.vector.div
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.sum
import org.mechdancer.algebra.implement.equation.builder.EquationSetBuilder
import org.mechdancer.algebra.implement.matrix.builder.foldToRows
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.to2D
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.algebra.uniqueValue
import org.mechdancer.geometry.angle.toAngle
import org.mechdancer.geometry.angle.toVector
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.abs

fun MatrixTransformation.transform(pose: Pose2D) =
    Pose2D(invoke(pose.p).to2D(), invokeLinear(pose.d.toVector()).to2D().toAngle())

/** 增量 [delta] 累加到里程 */
infix fun <T : Transformation<T>> T.plusDelta(delta: T): T =
    this * delta

/** 里程回滚到增量 [delta] 之前 */
infix fun <T : Transformation<T>> T.minusDelta(delta: T) =
    this * delta.inverse()

/** 计算里程从起点 [origin] 到当前状态的增量 */
infix fun <T : Transformation<T>> T.minusState(origin: T) =
    origin.inverse() * this

/**
 * 最小二乘法从点对集推算空间变换子
 * 任意维无约束
 */
fun PointMap.toTransformation(): MatrixTransformation? {
    val dim = sources
                  .map { it.dim }
                  .union(targets.map { it.dim })
                  .uniqueValue()
              ?: throw IllegalArgumentException("points not in same dim")
    val temp = DoubleArray(dim * dim)

    val ct = targets.sum() / size
    val cs = sources.sum() / size

    return takeIf { size > dim }
        ?.flatMap { (target, source) ->
            val arguments = (source - cs).toList()
            val results = target - ct

            List(dim) { r ->
                temp.fill(.0)
                arguments.forEachIndexed { i, it -> temp[dim * r + i] = it }
                val scale = temp.map(::abs).max()!!
                temp.toListVector() / scale to results[r] / scale
            }
        }
        ?.toSet()
        ?.solve()
        ?.toList()
        ?.foldToRows(dim)
        ?.let { MatrixTransformation.fromInhomogeneous(it, ct - it * cs) }
}

// 最小二乘法构造矩阵
// 将点对分别按重心平移
//   -> 从点集构造方程组
//   -> 解方程组
//   -> 从解构造矩阵
private fun Point2DMap.toTransformation(
    buildEquation: EquationSetBuilder.(Vector2D, Vector2D) -> Unit,
    buildMatrix: (Vector) -> Matrix
): MatrixTransformation? {
    if (size < 2) return null
    val ct = keys.sum() / size
    val cs = values.sum() / size

    return EquationSetBuilder()
        .also { builder ->
            for ((target, source) in this)
                buildEquation(builder, target - ct, source - cs)
        }
        .equationSet
        .solve()
        ?.let(buildMatrix)
        ?.let { MatrixTransformation.fromInhomogeneous(it, ct - it * cs) }
}

/**
 * 最小二乘法从点对集推算空间变换矩阵
 * 二维直角非手性
 */
fun Point2DMap.toTransformationOrthotropic(chiral: Boolean) =
    if (!chiral)
        toTransformation({ a, b ->
                             this[+b.x, -b.y] = a.x
                             this[+b.y, +b.x] = a.y
                         }) {
            matrix {
                row(+it[0], -it[1])
                row(+it[1], +it[0])
            }
        }
    else
        toTransformation({ a, b ->
                             this[+b.x, +b.y] = a.x
                             this[-b.y, +b.x] = a.y
                         }) {
            matrix {
                row(+it[0], +it[1])
                row(+it[1], -it[0])
            }
        }

/**
 * 求点对集在变换下基于某种距离计算的误差
 */
fun PointMap.errorBy(transformation: MatrixTransformation, type: DistanceType) =
    mapValues { transformation(it.value) }
        .toList()
        .sumByDouble(type::between)
        .div(size)
