package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.ValueMutableMatrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.*
import org.mechdancer.algebra.implement.matrix.Cofactor
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.*
import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import org.mechdancer.algebra.implement.matrix.special.NumberMatrix
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix
import org.mechdancer.algebra.implement.vector.listVectorOfZero
import org.mechdancer.algebra.implement.vector.toListVector
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

// scale multiply

private fun timesStub(m: Matrix, k: Double): Matrix =
	when (m) {
		is ZeroMatrix     -> m
		is NumberMatrix   -> NumberMatrix[m.row, m.column, m.value * k]
		is DiagonalMatrix -> DiagonalMatrix[m.diagonal.map { it * k }]
		else              -> m.toList().map { it * k }.foldToRows(m.row)
	}

operator fun Number.times(m: Matrix) = timesStub(m, this.toDouble())
operator fun Matrix.times(k: Number) = timesStub(this, k.toDouble())
operator fun Matrix.div(k: Number) = timesStub(this, k.toDouble())

// calculate between same size matrix

//逐项应用某种操作
private fun Matrix.zipAssign(
	other: Matrix,
	block: (Double, Double) -> Double
): Matrix {
	assertSameSize(this, other)
	return ListMatrix(column, toList().zip(other.toList(), block))
}

operator fun Matrix.plus(other: Matrix) = zipAssign(other) { a, b -> a + b }
operator fun Matrix.minus(other: Matrix) = zipAssign(other) { a, b -> a - b }

// times another linear algebra type

/**
 * 矩阵右乘向量
 */
operator fun Matrix.times(right: Vector): Vector {
	assertCanMultiply(this, right)
	return when {
		this is ZeroMatrix || right.isZero() ->
			listVectorOfZero(row)
		this is NumberMatrix                 ->
			right.select(0 until row) * value
		else                                 ->
			rows.map { it dot right }.toListVector()
	}
}

/**
 * 矩阵右乘矩阵
 */
operator fun Matrix.times(right: Matrix): Matrix {
	assertCanMultiply(this, right)
	return when {
		this is ZeroMatrix || right is ZeroMatrix ->
			ZeroMatrix[row, right.column]
		this is NumberMatrix                      ->
			timesStub(right, value)
		right is NumberMatrix                     ->
			timesStub(this, right.value)
		else                                      -> {
			val period = 0 until column
			listMatrixOf(row, right.column) { r, c ->
				period.sumByDouble { i -> this[r, i] * right[i, c] }
			}
		}
	}
}

/**
 * 矩阵右除矩阵
 */
operator fun Matrix.div(right: Matrix) = right.inverseOrNull()?.let { this * it }

operator fun Matrix.invoke(right: Matrix) = times(right)
operator fun Matrix.invoke(right: Vector) = times(right)
operator fun Matrix.invoke(right: Number) = times(right)

/**
 * 矩阵乘方
 */
infix fun Matrix.power(n: Int): Matrix {
	assertSquare()
	assert(n >= 0)
	return when (n) {
		0    -> I[dim]
		else -> {
			var temp = this
			for (i in 1 until n) temp *= this
			temp
		}
	}
}

operator fun Matrix.unaryPlus() = this
operator fun Matrix.unaryMinus() = ListMatrix(column, toList().map { -it })
fun Matrix.transpose() =
	if (isSymmetric())
		(this as? ValueMutableMatrix)?.clone() ?: this
	else listMatrixOf(column, row) { r, c -> this[c, r] }

fun Matrix.rowEchelon() = toArrayMatrix().rowEchelonAssign()

fun Matrix.cofactorOf(r: Int, c: Int) = Cofactor(this, r, c)

// 选择计算范围
private fun range(column: Int) =
	max(1E-8, min(1E-4, 10.0.pow(column * 0.2 - 12)))

/**
 * 范数
 * @param n 阶数
 */
fun Matrix.norm(n: Int = 2) =
	when (n) {
		-1   -> rows.map { it.norm(1) }.max()
		1    -> columns.map { it.norm(1) }.max()
		2    -> (transpose() * this)
			.jacobiLevelUp(1E-3, range(column))
			?.firstOrNull()
			?.first
			?.let(::sqrt)
		else -> throw UnsupportedOperationException("please invoke length(-1) for infinite length")
	} ?: Double.NaN

/**
 * 条件数
 * @param n 阶数
 */
fun Matrix.cond(n: Int = 2): Double {
	assertSquare()
	return norm(n) * inverse().norm(n)
}

/**
 * 求伴随矩阵
 */
fun Matrix.companion() = companionOrNull() ?: throw NotSquareException

/**
 * 用空表示伴随矩阵不存在
 */
fun Matrix.companionOrNull() =
	takeIf { isSquare() }?.let { listMatrixOf(row, column) { r, c -> algebraCofactorOf(c, r) } }

/**
 * 求不可变矩阵的逆矩阵
 */
fun Matrix.inverse(): Matrix {
	assertSquare()
	return inverseOrNull() ?: throw NotFullRankException
}

/**
 * 用空表示逆矩阵不存在
 */
fun Matrix.inverseOrNull() =
	when {
		// 不方，无法求逆
		isNotSquare()  -> null
		// 对角阵上各元素取倒数可得逆对角阵
		isDiagonal()   ->
			diagonal
				.takeIf { list -> list.all { it != .0 } }
				?.map { 1 / it }
				?.toDiagonalListMatrix()
		// 正交矩阵的转置与逆相等
		isOrthogonal() -> transpose()
		// 对于值可变矩阵，克隆可能有更高的效率，否则重新构造可变矩阵
		else           ->
			((this as? ValueMutableMatrix)
				?.clone()
				?: toArrayMatrix())
				.inverseDestructive()
	}

/**
 * 求实对称矩阵的迹（所有特征值的和）
 */
fun Matrix.trace() = jacobiLevelUp()?.sumByDouble { it.first }

object D {
	@JvmStatic
	operator fun invoke(matrix: Matrix) = matrix.det
}

object Det {
	@JvmStatic
	operator fun invoke(matrix: Matrix) = matrix.det
}

object R {
	@JvmStatic
	operator fun invoke(matrix: Matrix) = matrix.rank
}

object T {
	@JvmStatic
	operator fun invoke(matrix: Matrix) = matrix.trace()
}
