package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector

// 断言两个矩阵同型
internal fun assertSameSize(a: Matrix, b: Matrix) {
	if (!checkSameSize(a, b))
		throw UnsupportedOperationException("operate two matrix of different size (${a.row}*${a.column} and ${b.row}*${b.column})")
}

internal val Matrix.NotSquareException
	get() = UnsupportedOperationException("$row * $column is not a square")

internal val NotFullRankException
	get() = UnsupportedOperationException("matrix is not full-rank, so some operation is invalid")

// 断言矩阵是方阵
internal fun Matrix.assertSquare() {
	if (row != column) throw NotSquareException
}

// 断言矩阵能乘向量
internal fun assertCanMultiply(a: Matrix, b: Vector) {
	if (a.column != b.dim)
		throw UnsupportedOperationException("matrix of ${a.row}*${a.column} and ${b.dim}*1 is not match to multiply")
}

// 断言矩阵能相乘
internal fun assertCanMultiply(a: Matrix, b: Matrix) {
	if (a.column != b.row)
		throw UnsupportedOperationException("matrix of ${a.row}*${a.column} and ${b.row}*${b.column} is not match to multiply")
}
