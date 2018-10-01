package org.mechdancer.algebra.vector

import org.mechdancer.algebra.dimensionArgumentError
import org.mechdancer.algebra.vector.impl.Vector2D
import org.mechdancer.algebra.vector.impl.Vector3D
import org.mechdancer.algebra.vector.impl.VectorImpl

/**
 * Vector
 * 向量
 */
interface Vector {
	/**
	 * Dimension
	 * 维数
	 */
	val dimension: Int

	/**
	 * Get item on the [index] dim
	 * 获取指定维度上的值
	 */
	operator fun get(index: Int): Double

	/**
	 * Plus another vector, then return a new vector
	 * 加上一个向量并返回新的向量
	 */
	operator fun plus(other: Vector): Vector

	/**
	 * Plus a vector of opposite direction
	 * 加上一个与[other]反向的向量
	 */
	operator fun minus(other: Vector): Vector

	/**
	 * Multiply a number to return a new vector of different length
	 * 数乘，乘以系数改变向量的长度
	 */
	operator fun times(k: Double): Vector

	/**
	 * Dot product
	 * 点乘/内积/数量积
	 */
	infix fun dot(other: Vector): Double

	/**
	 * Norm
	 * 模长
	 */
	val norm: Double

	/**
	 * Transform to a [String] which occupies only one line
	 * 转为只占一行的字符串形式
	 */
	fun toSimpleString(): String

	/**
	 * Transform to a [Double] [List]
	 * 转为双精度浮点数列表
	 */
	fun toList(): List<Double>

	companion object {
		/**
		 * Build a vector from its data
		 * 从数字的列表构造向量
		 */
		operator fun invoke(data: List<Double>): Vector =
			if (data.isEmpty()) throw dimensionArgumentError
			else when (data.size) {
				2    -> Vector2D(data[0], data[1])
				3    -> Vector3D(data[0], data[1], data[2])
				else -> VectorImpl(data)
			}

		/** @return [dimension]维的零向量 */
		fun zeroOf(dimension: Int): Vector =
			if (dimension <= 0) throw dimensionArgumentError
			else
				when (dimension) {
					2    -> Vector2D(.0, .0)
					3    -> Vector3D(.0, .0, .0)
					else -> VectorImpl(List(dimension) { .0 })
				}
	}
}
