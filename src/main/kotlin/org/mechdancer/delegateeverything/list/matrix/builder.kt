package org.mechdancer.delegateeverything.list.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.Vector
import java.text.DecimalFormat

fun Vector.toListMatrix() = ListMatrix(1, toList())
fun Vector.toListMatrixRow() = ListMatrix(dimension, toList())

fun listMatrixOf(row: Int, column: Int, block: (Int, Int) -> Double) =
	ListMatrix(column, List(row * column) { block(it / column, it % column) })

fun listMatrixOfZero(row: Int, column: Int) =
	listMatrixOf(row, column) { _, _ -> .0 }

fun listMatrixOfUnit(dim: Int) =
	listMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

fun Matrix.matrixView(): String {
	val format = DecimalFormat(".##")
	val header = "$row x $column Matrix"
	//每列宽度
	val widths = columns.map { it.toList().map { num -> format.format(num).length }.max() ?: 0 }

	val blank = { length: Int -> " ".repeat(length) }
	val one = { c: Int, str: String ->
		val right = (widths[c] - str.length) / 2
		val left = widths[c] - str.length - right
		"${blank(left + 1)}$str${blank(right)}"
	}
	val line = { r: Int ->
		(0 until column).joinToString("") { c -> one(c, format.format(get(r, c))) }
	}
	return buildString {
		append(blank((widths.sum() + widths.size + 4 - header.length) / 2))
		appendln(header)
		if (row < 2)
			append("[${line(0)} ]")
		else {
			val pre = { r: Int ->
				when (r) {
					0       -> '┌'
					row - 1 -> '└'
					else    -> '│'
				}
			}
			val fix = { r: Int ->
				when (r) {
					0       -> '┐'
					row - 1 -> '┘'
					else    -> '│'
				}
			}
			append((0 until row).joinToString("\n") { r -> "${pre(r)}${line(r)} ${fix(r)}" })
		}
	}
}
