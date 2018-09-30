package org.mechdancer.delegateeverything.core

/**
 * Mutable Matrix
 * 可变矩阵
 */
interface MutableMatrix : ValueMutableMatrix {
	/**
	 * Add a row to this matrix
	 * 添加一行
	 */
	fun addRow(r: Int, vector: List<Double>)

	/**
	 * Add a column to this matrix
	 * 添加一列
	 */
	fun addColumn(c: Int, vector: List<Double>)

	/**
	 * Remove a row from this matrix
	 * 移除一行
	 */
	fun removeRow(r: Int)

	/**
	 * Remove a column from this matrix
	 * 移除一列
	 */
	fun removeColumn(c: Int)
}
