package org.mechdancer.delegateeverything.core

/**
 * Matrix
 * 矩阵
 */
interface Matrix {
	/**
	 * Number of rows
	 * 行数
	 */
	val row: Int

	/**
	 * Number of columns
	 * 列数
	 */
	val column: Int

	/**
	 * Get an element of this matrix
	 * 获取[r]行[c]列处的元素
	 */
	operator fun get(r: Int, c: Int): Double

	/**
	 * Get matrix of row vectors
	 * 获取行向量表示的矩阵
	 */
	val rows: List<Vector>

	/**
	 * Get matrix of column vectors
	 * 获取列向量表示的矩阵
	 */
	val columns: List<Vector>

	/**
	 * Get one row vector of this matrix
	 * 获取第[r]行的向量
	 */
	fun row(r: Int): Vector

	/**
	 * Get one column vector of this matrix
	 * 获取第[c]列的向量
	 */
	fun column(c: Int): Vector

	/**
	 * Get the rank of this matrix
	 * 获取矩阵的秩
	 */
	val rank: Int

	/**
	 * Calculate the det of this matrix
	 * 获取矩阵对应行列式的值
	 */
	val det: Double

	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
	override fun toString(): String
}
