package org.mechdancer.delegateeverything.implement.vector

import org.mechdancer.delegateeverything.core.Vector
import org.mechdancer.delegateeverything.implement.vector.DistanceType.*
import kotlin.math.abs
import kotlin.math.acos

operator fun Vector.times(k: Number) = toList().map { it * k.toDouble() }.let(::ListVector)
operator fun Vector.div(k: Number) = times(1 / k.toDouble())

private fun differentDimException(a: Vector, b: Vector) =
	IllegalArgumentException("operate two vector of different dim (${a.dim} and ${b.dim})")

private fun Vector.zip(other: Vector, block: (Double, Double) -> Double) =
	takeIf { dim == other.dim }
		?.let { toList().zip(other.toList(), block) }
		?: throw differentDimException(this, other)

operator fun Vector.plus(other: Vector) = zip(other) { a, b -> a + b }.let(::ListVector)
operator fun Vector.minus(other: Vector) = zip(other) { a, b -> a - b }.let(::ListVector)

operator fun Vector.unaryPlus() = this
operator fun Vector.unaryMinus() = toList().map { -it }.let(::ListVector)

infix fun Vector.dot(other: Vector) = zip(other) { a, b -> a * b }.sum()

fun Vector.normalize() = div(norm)

/** 计算两个向量之间的某种关系 */
enum class DistanceType(val between: (Vector, Vector) -> Double) {
	/** 用弧度表示的夹角 */
	IncludedAngle({ one, other -> acos((one dot other) / one.norm / other.norm) }),

	/** 欧式距离 */
	Euclid({ one, other -> (one - other).norm }),

	/** 曼哈顿距离 */
	Manhattan({ one, other -> (one - other).toList().sum() }),

	/** 切比雪夫距离 */
	Chebyshev({ one, other -> (one - other).toList().asSequence().map(::abs).max() ?: .0 });

	infix fun between(pair: Pair<Vector, Vector>) = between(pair.first, pair.second)
}

/** @return 与[other]间用弧度表示的夹角 */
infix fun Vector.includedAngle(other: Vector) = IncludedAngle.between(this, other)

/** @return 与[other]间的欧式距离 */
infix fun Vector.euclid(other: Vector) = Euclid.between(this, other)

/** @return 与[other]间的曼哈顿距离 */
infix fun Vector.manhattan(other: Vector) = Manhattan.between(this, other)

/** @return 与[other]间的切比雪夫距离 */
infix fun Vector.chebyshev(other: Vector) = Chebyshev.between(this, other)

fun distance(one: Vector, other: Vector, type: DistanceType) = type.between(one, other)

/** 复制向量，并进行指定的修改 */
fun Vector.copy(vararg change: Pair<Int, Number>): Vector {
	val map = change.associate { it }
	return List(dim) { map[it]?.toDouble() ?: get(it) }.let(::ListVector)
}

/** 从向量选取指定的维度组成新的向量 */
fun Vector.select(vararg index: Int): Vector =
	toList().filterIndexed { i, _ -> i in index }.let(::ListVector)

/** 从向量选取指定的维度组成新的向量 */
fun Vector.select(range: IntRange): Vector =
	toList().drop(range.first).take(range.last - range.first + 1).let(::ListVector)

/** 判断是否零向量 */
fun Vector.isZero() = norm == 0.0

/** 判断是否非零向量 */
fun Vector.isNotZero() = norm != 0.0

/** 判断是否单位向量 */
fun Vector.isNormalized() = norm == 1.0

/** 判断是否非单位向量 */
fun Vector.isNotNormalized() = norm != 1.0

//解绑定

operator fun Vector.component1() = this[0]
operator fun Vector.component2() = this[1]
operator fun Vector.component3() = this[2]
operator fun Vector.component4() = this[3]
operator fun Vector.component5() = this[4]

// 其中各维度的名字

val Vector.x get() = this[0]
val Vector.y get() = this[1]
val Vector.z get() = this[2]
