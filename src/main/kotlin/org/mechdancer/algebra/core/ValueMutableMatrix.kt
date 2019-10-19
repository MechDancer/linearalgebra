package org.mechdancer.algebra.core

import org.mechdancer.algebra.function.matrix.traceValue

/**
 * Matrix with mutable value and fixed size
 * 值可变，规格不可变的矩阵
 */
interface ValueMutableMatrix : Matrix, Cloneable {
	/**
	 * Set an element of this matrix
	 * 设置[r]行[c]列处的元素
	 */
	operator fun set(r: Int, c: Int, value: Double)

	/**
	 * Set a whole line
	 * 修改一整行
	 */
	fun setRow(r: Int, vector: List<Double>)

	/**
	 * Set a whole column
	 * 修改一整列
	 */
	fun setColumn(c: Int, vector: List<Double>)

	/**
	 * Elementary transformation: multiply the row [r] with [k]
     * 初等行变换：第 [r] 行数乘 [k]
	 */
	fun timesRow(r: Int, k: Double)

	/**
     * Elementary transformation: multiply the row [r0] with [k], and add it into row [r1]
     * 初等行变换：[k] 倍 [r0] 加到 [r1]
	 */
	fun plusToRow(k: Double, r0: Int, r1: Int)

	/**
     * Elementary transformation: swap the row [r0] with row [r1]
     * 初等行变换：[r0] 和 [r1]交换
	 */
	fun exchangeRow(r0: Int, r1: Int)

	/**
     * Elementary transformation: multiply the column [c] with [k]
     * 初等列变换：第 [c] 列数乘 [k]
	 */
	fun timesColumn(c: Int, k: Double)

	/**
     * Elementary transformation: multiply the column [c0] with [k], and add it into column [c1]
	 * 初等列变换：[k] 倍 [c0] 加到 [c1]
	 */
	fun plusToColumn(k: Double, c0: Int, c1: Int)

	/**
     * Elementary transformation: swap the column [c0] with column [c1]
     * 初等列变换：[c0]和[c1]交换
	 */
	fun exchangeColumn(c0: Int, c1: Int)

	override val trace get() = traceValue()

	public override fun clone(): ValueMutableMatrix
}
