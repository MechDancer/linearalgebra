package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.ValueMutableMatrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.dot
import org.mechdancer.algebra.function.vector.norm
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.Cofactor
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.*
import org.mechdancer.algebra.implement.vector.toListVector

//矩阵转换为List<Double>
private fun Matrix.toList() =
	(this as? ListMatrix)?.data
		?: (this as? ArrayMatrix)?.data?.toList()
		?: rows.flatMap { it.toList() }

operator fun Number.times(m: Matrix): Matrix {
	val temp = toDouble()
	return ListMatrix(m.column, m.toList().map { it * temp })
}

operator fun Matrix.times(k: Number) = k * this
operator fun Matrix.div(k: Number) = (1 / k.toDouble()) * this

//逐项应用某种操作
private fun Matrix.zip(other: Matrix, block: (Double, Double) -> Double): Matrix {
	assertSameSize(this, other)
	return ListMatrix(column, toList().zip(other.toList(), block))
}

operator fun Matrix.plus(other: Matrix) = zip(other) { a, b -> a + b }
operator fun Matrix.minus(other: Matrix) = zip(other) { a, b -> a - b }

operator fun Matrix.times(vector: Vector): Vector {
	assertCanMultiply(this, vector)
	return rows.map { it dot vector }.toListVector()
}

operator fun Matrix.times(other: Matrix): Matrix {
	assertCanMultiply(this, other)
	val period = 0 until column
	return listMatrixOf(row, other.column) { r, c ->
		period.sumByDouble { i -> this[r, i] * other[i, c] }
	}
}

operator fun Matrix.div(other: Matrix) = other.inverseOrNull()?.let { it * this }

operator fun Matrix.invoke(right: Matrix) = times(right)
operator fun Matrix.invoke(right: Vector) = times(right)
operator fun Matrix.invoke(right: Number) = times(right)

infix fun Matrix.power(n: Int): Matrix {
	assertSquare()
	assert(n >= 0)
	return when (n) {
		0    -> I(dim)
		else -> {
			var temp = this
			for (i in 1 until n) temp *= this
			temp
		}
	}
}

operator fun Matrix.unaryPlus() = this
operator fun Matrix.unaryMinus() = ListMatrix(column, toList().map { -it })
fun Matrix.transpose() = listMatrixOf(column, row) { r, c -> this[c, r] }
fun Matrix.rowEchelon() = toArrayMatrix().rowEchelonAssign()

fun Matrix.cofactorOf(r: Int, c: Int) = Cofactor(this, r, c)

private fun Matrix.algebraCofactorOf(r: Int, c: Int): Double =
	(if ((r + c) % 2 == 0) 1 else -1) * cofactorOf(r, c).determinantValue()

/**
 * 范数
 * @param n 阶数
 */
fun Matrix.norm(n: Int = 2) =
	when (n) {
		-1   -> rows.map { it.norm(1) }.max()
		1    -> columns.map { it.norm(1) }.max()
		2    -> ((transpose() * this) jacobiMethod 1E-6).map { it.first }.max()
		else -> throw UnsupportedOperationException("please invoke length(-1) for infinite length")
	} ?: Double.NaN

/**
 * 条件数
 * @param n 阶数
 */
fun Matrix.cond(n: Int = 2) =
	norm(n) * inverse().norm(n)

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
 * 计算矩阵的行列式值
 * 不会访问矩阵的行列式缓存，因此可用于实现类内
 */
fun Matrix.determinantValue() =
	if (isNotSquare()) .0
	else when (row) {
		1    -> get(0, 0)
		2    -> get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0)
		rank -> (0 until column).sumByDouble { c ->
			get(0, c) * algebraCofactorOf(0, c)
		}
		else -> .0
	}

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
fun Matrix.trace() = jacobiMethod(1E-6).sumByDouble { it.first }

object D {
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

fun main(args: Array<String>) {
	matrix {
		row(5, 7)
		row(7, 10)
	}.cond().let(::println)
}
