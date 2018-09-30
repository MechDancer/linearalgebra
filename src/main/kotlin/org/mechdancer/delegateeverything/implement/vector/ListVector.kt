package org.mechdancer.delegateeverything.implement.vector

import org.mechdancer.delegateeverything.core.Vector
import org.mechdancer.delegateeverything.core.columnView
import kotlin.math.sqrt

/**
 * [Vector] delegated by [List] of [Double]
 * 用列表代理实现的向量
 */
class ListVector(data: List<Double>)
	: Vector by VectorCore(data) {
	private class VectorCore(val data: List<Double>)
		: Vector, List<Double> by data {
		override val dimension = size
		override val norm by lazy { sqrt(sumByDouble { it * it }) }
		override fun toList() = data

		override fun equals(other: Any?) =
			other is Vector && other.toList() == data

		override fun hashCode() = data.hashCode()
		override fun toString() = columnView()
	}
}
