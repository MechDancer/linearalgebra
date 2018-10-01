package org.mechdancer.delegateeverything.implement.vector

import org.mechdancer.delegateeverything.core.Vector
import org.mechdancer.delegateeverything.core.columnView
import kotlin.math.sqrt

/**
 * [Vector] delegated by [List] of [Double]
 * 用列表代理实现的向量
 */
class ListVector(val data: List<Double>) : Vector {
	override val dim = data.size
	override fun get(i: Int) = data[i]
	override val norm by lazy { sqrt(data.sumByDouble { it * it }) }
	override fun toList() = data

	override fun equals(other: Any?) =
		other is Vector && other.toList() == data

	override fun hashCode() = data.hashCode()
	override fun toString() = columnView()
}
