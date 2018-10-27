package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import org.mechdancer.algebra.implement.matrix.special.HilbertMatrix
import org.mechdancer.algebra.implement.matrix.special.NumberMatrix
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix
import kotlin.math.min

// Matrix.toList()
// Put all elements in the matrix in a list of double
// 获取矩阵内数据

fun ZeroMatrix.toList() = List(row * column) { .0 }
fun ListMatrix.toList() = data
fun ArrayMatrix.toList() = data.toList()
fun Matrix.toList() = List(row * column) { get(it / column, it % column) }

// Matrix.toSet()
// Put all elements in the matrix in a set of double
// 获取矩阵内所有不同的数据

fun ZeroMatrix.toSet() = setOf(.0)
fun NumberMatrix.toSet() = setOf(value)
fun HilbertMatrix.toSet() = List(row + column) { 1.0 / (it + 1) }.toSet()
fun ListMatrix.toSet() = data.toSet()
fun ArrayMatrix.toSet() = data.toSet()

fun DiagonalMatrix.toSet(): Set<Double> =
	diagonal.toMutableSet().apply { add(.0) }

fun Matrix.toSet(): Set<Double> =
	mutableSetOf<Double>().apply {
		for (r in 0 until row)
			for (c in 0 until column)
				add(get(r, c))
	}

/**
 * Get the dimension of this square, for non-square matrix it will be -1
 * 获取方阵的维数，非方阵的维数定义为 -1
 */
val Matrix.dim get() = if (row == column) row else -1

/**
 * Get the first row of a matrix
 * 获取矩阵的第一行
 */
val Matrix.firstRow get() = row(0)

/**
 * Get the last row of a matrix
 * 获取矩阵的最后一行
 */
val Matrix.lastRow get() = row(row - 1)

/**
 * Get the first column of a matrix
 * 获取矩阵的第一列
 */
val Matrix.firstColumn get() = column(0)

/**
 * Get the last column of a matrix
 * 获取矩阵的最后一列
 */
val Matrix.lastColumn get() = column(column - 1)

// Matrix.diagonal
// Get the main diagonal of a matrix
// 获取矩阵主对角线

val ZeroMatrix.diagonal get() = List(min(row, column)) { .0 }
val NumberMatrix.diagonal get() = List(dim) { value }
val Matrix.diagonal get() = List(min(row, column)) { get(it, it) }

/**
 * Filter elements with index on rows and columns
 * 带二维序号过滤
 */
inline fun Matrix.filterIndexed(block: (Int, Int, Double) -> Boolean) =
	(0 until row).flatMap { r ->
		(0 until column).map { c ->
			get(r, c).takeIf { block(r, c, it) }?.let { r to c to it }
		}
	}.filterNotNull()

/**
 * Map elements with index on rows and columns
 * 带二维序号展开
 */
inline fun <T> Matrix.mapIndexed(block: (Int, Int, Double) -> T) =
	(0 until row).flatMap { r ->
		(0 until column).map { c ->
			block(r, c, get(r, c))
		}
	}
