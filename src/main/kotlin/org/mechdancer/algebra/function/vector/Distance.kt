package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.DistanceType.*
import kotlin.math.abs
import kotlin.math.acos

/** 计算两个向量之间的某种距离 */
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
