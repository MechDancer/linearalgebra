package org.mechdancer.delegateeverything.list.vector

import org.mechdancer.delegateeverything.core.Vector
import java.text.DecimalFormat

fun Iterable<Number>.toListVector() =
	map { it.toDouble() }.let(::ListVector)

fun Array<Number>.toListVector() =
	toList().toListVector()

fun listVectorOf(vararg item: Number) =
	item.map { it.toDouble() }.toListVector()

fun listVectorOfZero(dim: Int) =
	List(dim) { .0 }.toListVector()

fun Vector.columnView(): String {
	val format = DecimalFormat(".##")
	val header = "${dimension}D Vector"
	val string = toList().asSequence().map(format::format)
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
