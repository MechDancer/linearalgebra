package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.Vector

//矩阵转换为List<Double>
private fun Matrix.toList() =
	(this as? ListMatrix)?.list
		?: (this as? ArrayMatrix)?.array?.toList()
		?: rows.flatMap { it.toList() }

operator fun Matrix.times(k: Number) =
	ListMatrix(column, toList().map { it * k.toDouble() })

operator fun Matrix.div(k: Number) = times(1 / k.toDouble())

private fun differentSizeException(a: Matrix, b: Matrix) =
	IllegalArgumentException("operate two matrix of different size (${a.row}*${a.column} and ${b.row}*${b.column})")

private fun Matrix.zip(other: Matrix, block: (Double, Double) -> Double) =
	takeIf { row == other.row && column == other.column }
		?.let { toList().zip(other.toList(), block) }
		?: throw differentSizeException(this, other)

operator fun Matrix.plus(other: Matrix) = ListMatrix(column, zip(other) { a, b -> a + b })
operator fun Matrix.minus(other: Matrix) = ListMatrix(column, zip(other) { a, b -> a - b })

operator fun Matrix.times(other: Matrix) =
	takeIf { column == other.row }
		?.run {
			val size = 0 until column
			listMatrixOf(row, other.column) { r, c ->
				size.sumByDouble { i -> this[r, i] * other[i, c] }
			}
		}
		?: throw IllegalArgumentException("column of left matrix must equals to row of right matrix")

operator fun Matrix.times(vector: Vector) =
	times(vector.toListMatrix())

operator fun Matrix.invoke(right: Matrix) = times(right)
operator fun Matrix.invoke(right: Vector) = times(right)
operator fun Matrix.invoke(right: Number) = times(right.toDouble())

operator fun Matrix.unaryPlus() = this
operator fun Matrix.unaryMinus() = ListMatrix(column, toList().map { -it })

fun Matrix.isSquare() = row == column
fun Matrix.isNotSquare() = row != column

fun Matrix.transpose() = listMatrixOf(column, row) { r, c -> this[c, r] }
fun Matrix.toRowEchelon() = toArrayMatrix().rowEchelon()

fun Matrix.getCofactor(r: Int, c: Int) =
	tomutableListMatrix().apply {
		removeRow(r)
		removeColumn(c)
	}
