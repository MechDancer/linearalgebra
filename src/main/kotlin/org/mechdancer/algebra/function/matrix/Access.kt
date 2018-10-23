package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix

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

/**
 * Map elements with index on rows and columns
 * 带二维序号展开
 */
fun <T> Matrix.mapIndexed(block: (Int, Int, Double) -> T) =
	(0 until row).flatMap { r ->
		(0 until column).map { c ->
			block(r, c, get(r, c))
		}
	}
