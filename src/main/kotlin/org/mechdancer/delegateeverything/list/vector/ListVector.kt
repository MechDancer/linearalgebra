package org.mechdancer.delegateeverything.list.vector

import org.mechdancer.delegateeverything.core.Vector
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

		override fun toString(): String {
			val header = "${dimension}D ListVector"
			val string = toList().asSequence().map { it.toString() }
			val maxDataLength = kotlin.math.max((string.map { it.length }.max() ?: 0) + 2, header.length - 2)
			val pre = { index: Int ->
				when (index) {
					0                  -> '┌'
					toList().lastIndex -> '└'
					else               -> '│'
				}
			}
			val fix = { index: Int ->
				when (index) {
					0                  -> '┐'
					toList().lastIndex -> '┘'
					else               -> '│'
				}
			}
			val blank = { length: Int -> " ".repeat(length) }
			val transform = { i: Int, str: String ->
				val right = (maxDataLength - str.length) / 2
				val left = maxDataLength - str.length - right
				"${pre(i)}${blank(left)}$str${blank(right)}${fix(i)}"
			}
			return buildString {
				append(blank((maxDataLength + 3 - header.length) / 2))
				appendln(header)
				append(string.mapIndexed(transform).joinToString("\n"))
			}
		}
	}
}
