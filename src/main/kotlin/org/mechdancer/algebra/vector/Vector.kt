package org.mechdancer.algebra.vector

import org.mechdancer.algebra.dimensionArgumentError
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.vector.impl.Vector2D
import org.mechdancer.algebra.vector.impl.Vector3D
import org.mechdancer.algebra.vector.impl.VectorImpl

/**
 * 向量
 */
interface Vector {
	/** 维数 */
	val dimension: Int

	/** 数据 */
	val data: MatrixElement

	/** @return 第[index]维上的数据 */
	operator fun get(index: Int): Double

	/** 相加 */
	operator fun plus(other: Vector): Vector

	/** 相减 */
	operator fun minus(other: Vector): Vector

	/** 数乘 */
	operator fun times(k: Double): Vector

	/** 数除 */
	operator fun div(k: Double): Vector

	/** 内积 */
	infix fun dot(other: Vector): Double

	/** @return 相反向量 */
	operator fun unaryMinus(): Vector

	/** @return 模长 */
	fun norm(): Double

	/** @return 单位向量 */
	fun normalize(): Vector

	/** @return 用弧度表示的与[other]夹角 */
	fun includedAngle(other: Vector): Double

	/** 转简单字符串 */
	fun toSimpleString(): String

	companion object {
		/** 假构造器 */
		operator fun invoke(data: MatrixElement): Vector =
			if (data.isEmpty()) dimensionArgumentError()
			else when (data.size) {
				2    -> Vector2D(data[0], data[1])
				3    -> Vector3D(data[0], data[1], data[2])
				else -> VectorImpl(data)
			}

		/** @return [dimension]维的零向量 */
		fun zeroOf(dimension: Int): Vector =
			if (dimension <= 0) dimensionArgumentError()
			else
				when (dimension) {
					2    -> Vector2D(.0, .0)
					3    -> Vector3D(.0, .0, .0)
					else -> DoubleArray(dimension).toList().toVector()
				}
	}
}
