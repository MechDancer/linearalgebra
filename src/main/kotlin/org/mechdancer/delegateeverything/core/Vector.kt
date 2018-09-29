package org.mechdancer.delegateeverything.core

interface Vector {
	/**
	 * Dimension
	 * 维数
	 */
	val dimension: Int

	/**
	 * Get item on the [index] dimension
	 * 获取指定维度上的值
	 */
	operator fun get(index: Int): Double

	/**
	 * Norm
	 * 模长
	 */
	val norm: Double

	/**
	 * Transform to a [List] of [Double]
	 * 转为双精度浮点数列表
	 */
	fun toList(): List<Double>

	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
	override fun toString(): String
}
