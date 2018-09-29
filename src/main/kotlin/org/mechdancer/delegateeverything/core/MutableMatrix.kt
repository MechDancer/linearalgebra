package org.mechdancer.delegateeverything.core

/**
 * 值可变，规格不可变的矩阵
 */
interface ValueMutableMatrix : Matrix {
	/**
	 * Set an element of this matrix
	 * 设置[r]行[c]列处的元素
	 */
	operator fun set(r: Int, c: Int, value: Double)

	/**
	 * Set an whole line
	 * 修改一整行
	 */
	fun setRow(r: Int, value: Vector)

	/**
	 * Set an whole column
	 * 修改一整列
	 */
	fun setColumn(c: Int, value: Vector)

	/**
	 * 初等行变换：数乘
	 */
	fun timesRow(r: Int, k: Double)

	/**
	 * 初等行变换：[r0]加到[r1]
	 */
	fun plusToRow(r0: Int, r1: Int)

	/**
	 * 初等行变换：[r0]和[r1]交换
	 */
	fun exchangeRow(r0: Int, r1: Int)

	/**
	 * 初等列变换：数乘
	 */
	fun timesColumn(c: Int, k: Double)

	/**
	 * 初等列变换：[c0]加到[c1]
	 */
	fun plusToColumn(c0: Int, c1: Int)

	/**
	 * 初等列变换：[c0]和[c1]交换
	 */
	fun exchangeColumn(c0: Int, c1: Int)
}
