package org.mechdancer.delegateeverything.list.matrix

import org.mechdancer.delegateeverything.core.Matrix

private fun Matrix.toList() =
	(this as? ListMatrix)?.data ?: rows.flatMap { it.toList() }

operator fun Matrix.times(k: Double) =
	ListMatrix(column, toList().map { it * k })

operator fun Matrix.div(k: Double) = times(1 / k)

private fun differentSizeException(a: Matrix, b: Matrix) =
	IllegalArgumentException("operate two matrix of different size (${a.row}*${a.column} and ${b.row}*${b.column})")

private fun Matrix.zip(other: Matrix, block: (Double, Double) -> Double) =
	takeIf { row == other.row && column == other.column }
		?.let { toList().zip(other.toList(), block) }
		?: throw differentSizeException(this, other)

operator fun Matrix.plus(other: Matrix) = ListMatrix(column, zip(other) { a, b -> a + b })
operator fun Matrix.minus(other: Matrix) = ListMatrix(column, zip(other) { a, b -> a - b })

operator fun Matrix.unaryPlus() = this
operator fun Matrix.unaryMinus() = ListMatrix(column, toList().map { -it })

fun Matrix.isSquare() = row == column
fun Matrix.isNotSquare() = row != column

fun Matrix.transpose() = listMatrixOf(column, row) { r, c -> this[c, r] }
