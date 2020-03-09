package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.function.matrix.plus
import org.mechdancer.algebra.function.matrix.svd
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.matrix.transpose
import org.mechdancer.algebra.function.vector.DistanceType
import org.mechdancer.algebra.function.vector.div
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.sum
import org.mechdancer.algebra.implement.equation.builder.EquationSetBuilder
import org.mechdancer.algebra.implement.matrix.builder.foldToRows
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.matrix.builder.toListMatrix
import org.mechdancer.algebra.implement.matrix.builder.toListMatrixRow
import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.to2D
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.algebra.implement.vector.vector3D
import org.mechdancer.geometry.angle.toAngle
import org.mechdancer.geometry.angle.toVector
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.abs
import kotlin.random.Random

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
fun Collection<Pair<Vector, Vector>>.toTransformation(): MatrixTransformation? {
    val ct = asSequence().map { (a, _) -> a }.sum() / size
    val cs = asSequence().map { (_, b) -> b }.sum() / size
    val dim = ct.dim
    val temp = DoubleArray(dim * dim)

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

/** 点云配准 := {(目标，初始)} */
fun Collection<Pair<Vector, Vector>>.toTransformationWithSVD(): MatrixTransformation {
    val ct = map { (a, _) -> a }.sum() / size
    val cs = map { (_, b) -> b }.sum() / size
    val w = fold(ZeroMatrix[cs.dim] as Matrix)
    { r, (t, s) -> r + (t - ct).toListMatrix() * (s - cs).toListMatrixRow() }
    val (u, sigma, v) = w.svd()
    require(u * DiagonalMatrix(sigma) * v.transpose() == w) // FIXME 奇异值分解没错，结果为什么不对？
    val r = u * v.transpose()
    val t = ct - r * cs
    return MatrixTransformation.fromInhomogeneous(r, t)
}

fun main() {
    val pose = Pose3D(vector3D(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()),
                      vector3D(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()))
    val list = List(20) { vector3D(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()) }
    val map = list.map { pose * it to it }
    println(pose.toMatrixTransformation())
    println(map.toTransformation())
    println(map.toTransformationWithSVD())
}
