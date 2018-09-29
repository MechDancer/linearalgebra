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
	val width = kotlin.math.max((string.map { it.length }.max() ?: 0) + 2, header.length - 2)
	val blank = { length: Int -> " ".repeat(length) }
	val one = { str: String ->
		val right = (width - str.length) / 2
		val left = width - str.length - right
		"${blank(left)}$str${blank(right)}"
	}
	return buildString {
		append(blank((width + 3 - header.length) / 2))
		appendln(header)
		val transform =
			if (dimension < 2)
				{ _: Int, str: String -> "[${one(str)}]" }
			else {
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
				{ i: Int, str: String -> "${pre(i)}${one(str)}${fix(i)}" }
			}
		append(string.mapIndexed(transform).joinToString("\n"))
	}
}
