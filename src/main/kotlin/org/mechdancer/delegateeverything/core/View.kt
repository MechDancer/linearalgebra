package org.mechdancer.delegateeverything.core

import java.text.DecimalFormat

//转边框字符（一行）
private fun trans(default: Char) = { _: Int -> default }

//转边框字符（多行）
private fun trans(lastIndex: Int, a: Char, b: Char, c: Char) =
	{ i: Int ->
		when (i) {
			0         -> a
			lastIndex -> c
			else      -> b
		}
	}

//边框函数
private fun border(lastIndex: Int) =
	if (lastIndex == 0)
		trans('[') to trans(']')
	else
		trans(lastIndex, '┌', '│', '└') to trans(lastIndex, '┐', '│', '┘')

//双精度浮点格式工具
private val formatter = DecimalFormat(".##")

//格式化双精度浮点数
private fun format(value: Double) =
	when (value) {
		in -5E-3..+5E-3 -> "_"
		else            -> formatter.format(value)
	}

//构造指定长度的空白
private fun blank(length: Int) = " ".repeat(length)

/**
 * 将向量以列形式转换为字符串
 */
fun Vector.columnView(): String {
	//标头
	val header = "${dimension}D Vector"
	//缓存字符串
	val string = toList().asSequence().map(formatter::format)
	//总宽度
	val width = kotlin.math.max((string.map { it.length }.max() ?: 0) + 2, header.length - 2)
	//变换一项
	val one = { str: String ->
		val right = (width - str.length) / 2
		val left = width - str.length - right
		"${blank(left)}$str${blank(right)}"
	}
	return buildString {
		append(blank((width + 3 - header.length) / 2))
		appendln(header)
		val (pre, fix) = border(dimension - 1)
		append(string.mapIndexed { i, str -> "${pre(i)}${one(str)}${fix(i)}" }.joinToString("\n"))
	}
}

/**
 * 将矩阵显示成字符串
 */
fun Matrix.matrixView(): String {
	//标头
	val header = "$row x $column Matrix"
	//每列宽度
	val widths = columns.map { it.toList().map { num -> format(num).length }.max() ?: 0 }
	//变换一项
	val one = { c: Int, str: String ->
		val right = (widths[c] - str.length) / 2
		val left = widths[c] - str.length - right
		"${blank(left + 1)}$str${blank(right)}"
	}
	//变换一行
	val line = { r: Int ->
		(0 until column).joinToString("") { c -> one(c, format(get(r, c))) }
	}
	return buildString {
		append(blank((widths.sum() + widths.size + 4 - header.length) / 2))
		appendln(header)
		val (pre, fix) = border(row - 1)
		append((0 until row).joinToString("\n") { r -> "${pre(r)}${line(r)} ${fix(r)}" })
	}
}
