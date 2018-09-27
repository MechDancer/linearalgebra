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

/** 复制向量，并进行指定的修改 */
fun Vector.copy(vararg change: Pair<Int, Double>): Vector {
	val map = change.associate { it }
	return List(dimension) { map[it] ?: this[it] }.toVector()
}

/** 从向量选取指定的维度组成新的向量 */
fun Vector.select(vararg index: Int): Vector =
	data.filterIndexed { i, _ -> i in index }.toVector()

/** 从向量选取指定的维度组成新的向量 */
fun Vector.select(range: IntRange): Vector =
	data.drop(range.first).take(range.last - range.first + 1).toVector()

operator fun Vector.component1() = this[0]
operator fun Vector.component2() = this[1]
operator fun Vector.component3() = this[2]
operator fun Vector.component4() = this[3]
operator fun Vector.component5() = this[4]

val Vector.x get() = this[0]
val Vector.y get() = this[1]
val Vector.z get() = this[2]
