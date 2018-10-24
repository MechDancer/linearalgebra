package org.mechdancer.algebra.core

/**
 * Vector
 * 向量
 */
interface Vector {
	/**
	 * Dimension
	 * 维数
	 */
	val dim: Int

	/**
	 * Get item on the [i] dim
	 * 获取指定维度上的值
	 */
	operator fun get(i: Int): Double

	/**
	 * Norm
	 * 模长
	 */
	val length: Double

	/**
	 * Transform to a [List] of [Double]
	 * 转为双精度浮点数列表
	 */
	fun toList(): List<Double>

	override fun equals(other: Any?): Boolean
	override fun hashCode(): Int
	override fun toString(): String
}
