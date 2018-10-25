package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.implement.vector.toListVector

/**
 * 求代数余子式
 */
internal fun Matrix.algebraCofactorOf(r: Int, c: Int): Double =
	(if ((r + c) % 2 == 0) 1 else -1) * cofactorOf(r, c).determinantValue()

/**
 * 通用方法计算矩阵的行列式值
 */
internal fun Matrix.determinantValue() =
	if (row != column) .0
	else when (row) {
		1    -> get(0, 0)
		2    -> get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0)
		rank -> (0 until column).sumByDouble { c ->
			get(0, c) * algebraCofactorOf(0, c)
		}
		else -> .0
	}

internal fun Matrix.getRow(r: Int) =
	List(column) { c -> get(r, c) }.toListVector()

internal fun Matrix.getColumn(c: Int) =
	List(row) { r -> get(r, c) }.toListVector()

internal fun Matrix.getRows() = List(row, ::getRow)
internal fun Matrix.getColumns() = List(column, ::getColumn)

internal fun checkSameSize(a: Matrix, b: Matrix) =
	a.row == b.row && a.column == b.column

internal fun checkElementsEquals(a: Matrix, b: Matrix): Boolean {
	for (r in 0 until a.row)
		for (c in 0 until a.column)
			if (!doubleEquals(a[r, c], b[r, c]))
				return false
	return true
}
