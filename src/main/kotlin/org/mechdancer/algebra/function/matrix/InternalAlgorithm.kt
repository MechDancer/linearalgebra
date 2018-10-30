package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.toListVector

// 开发者注意：
// 此文件中的函数用于实现无特殊实现的矩阵，因此应谨慎使用可能被重写的属性，以免发生意外的递归
// 一定可以安全使用的属性和方法包括：
// row       获取行数
// column    获取列数
// get(r, c) 获取元素的值

// 检查两矩阵同型
internal fun checkSameSize(a: Matrix, b: Matrix) =
	a.row == b.row && a.column == b.column

// 比较所有元素对应相同
// 使用前应先判断两矩阵同型
internal fun checkElementsEquals(a: Matrix, b: Matrix): Boolean {
	for (r in 0 until a.row)
		for (c in 0 until a.column)
			if (!doubleEquals(a[r, c], b[r, c]))
				return false
	return true
}

// 取一行
internal fun Matrix.getRow(r: Int) =
	List(column) { c -> get(r, c) }.toListVector()

// 取一列
internal fun Matrix.getColumn(c: Int) =
	List(row) { r -> get(r, c) }.toListVector()

// 取所有行
internal fun Matrix.getRows() = List(row, ::getRow)

// 取所有列
internal fun Matrix.getColumns() = List(column, ::getColumn)

// 求代数余子式
internal fun Matrix.algebraCofactorOf(r: Int, c: Int): Double? =
	if (row != column) null
	else cofactorOf(r, c).determinantValue()!! * if ((r + c) % 2 == 0) 1 else -1

// 计算矩阵的行列式值
internal fun Matrix.determinantValue(): Double? {
	if (row != column) return null
	val temp = toArrayMatrix()
	val scale = temp.rowEchelonAssign()
	return scale * (0 until dim).fold(1.0) { acc, i -> acc * temp[i, i] }
}

