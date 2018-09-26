package org.mechdancer.algebra.vector

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.vector.impl.VectorImpl
import kotlin.math.acos

/**
 * Convert a MatrixElement into a Vector
 * See [MatrixElement]
 * @receiver MatrixElement
 * @return result
 */
fun MatrixElement.toVector(): Vector = VectorImpl(this)

/**
 * Convert a matrix into a Vector
 * Matrix must be either single column or single row.
 * @receiver matrix
 * @return   result
 */
fun Matrix.toVector(): Vector = when {
	data.size == 1         -> data[0]
	data.first().size == 1 -> List(data.size) { data[it].first() }

	else                   -> throw IllegalArgumentException("matrix isn't single row or column exclusively")
}.toVector()

/**
 * Build a vector from doubles
 * @param double doubles
 */
fun vectorOf(vararg double: Double): Vector = VectorImpl(double.toList())

/** 数除 */
operator fun Vector.div(k: Double) = this * (1 / k)

/** @return 相反向量 */
operator fun Vector.unaryMinus() = this * -1.0

/** @return 单位向量 */
fun Vector.normalize() = this / norm()

/** 计算两个向量之间的某种关系 */
enum class Relation(val between: (Vector, Vector) -> Double) {
	/** 用弧度表示的夹角 */
	IncludedAngle({ one, other -> acos(one dot other / one.norm() / other.norm()) }),

	/** 欧式距离 */
	Euclid({ one, other -> (one - other).norm() }),

	/** 曼哈顿距离 */
	Manhattan({ one, other -> (one - other).data.sum() }),
}

/** @return 与[other]间用弧度表示的夹角 */
infix fun Vector.includedAngle(other: Vector) = Relation.IncludedAngle.between(this, other)

/** @return 与[other]间的欧式距离 */
infix fun Vector.euclid(other: Vector) = Relation.Euclid.between(this, other)

/** @return 与[other]间的曼哈顿距离 */
infix fun Vector.manhattan(other: Vector) = Relation.Manhattan.between(this, other)
