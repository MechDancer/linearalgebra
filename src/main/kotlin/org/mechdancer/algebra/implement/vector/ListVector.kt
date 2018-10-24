package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import org.mechdancer.algebra.doubleEquals
import kotlin.math.sqrt

/**
 * Vector of List of Double
 * 用浮点列表实现的向量
 */
class ListVector(val data: List<Double>) : Vector {
	override val dim = data.size
	override fun get(i: Int) = data[i]
	override val length by lazy { sqrt(data.sumByDouble { it * it }) }
	override fun toList() = data

	override fun equals(other: Any?) =
		other is Vector && data.zip(other.toList(), ::doubleEquals).all { it }

	override fun hashCode() = data.hashCode()
	override fun toString() = columnView()
}
