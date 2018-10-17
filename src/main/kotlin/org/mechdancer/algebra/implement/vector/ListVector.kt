package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import kotlin.math.sqrt

/**
 * Vector of List of Double
 * 用浮点列表实现的向量
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

fun main(args: Array<String>) {
	ListVector(listOf()).let(::println)
}
