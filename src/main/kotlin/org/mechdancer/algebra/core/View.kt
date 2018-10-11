package org.mechdancer.algebra.core

import org.mechdancer.algebra.function.equation.args
import org.mechdancer.algebra.function.equation.constants
import org.mechdancer.algebra.implement.equation.builder.toMatrixForm
import org.mechdancer.algebra.implement.matrix.builder.toListMatrix
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
private val formatter = DecimalFormat("#.##")

//格式化双精度浮点数
private fun format(value: Double) =
	when (value) {
		in -5E-3..+5E-3 -> "_"
		else            -> formatter.format(value)
	}

//构造指定长度的空白
private fun blank(length: Int) = " ".repeat(length)

//变换一项 : 向两边添加空格，把元素补到所在列的宽度
private fun enlarge(str: String, width: Int): String {
	val left = (width - str.length + 1) / 2
	val right = width - str.length - left
	return "${blank(left)}$str${blank(right)}"
}

/**
 * Convert a column vector to string
 * 将向量以列形式转换为字符串
 */
fun Vector.columnView() =
	toListMatrix().matrixView("${dim}D Vector")

/**
 * Convert the matrix to string
 * 将矩阵显示成字符串
 */
fun Matrix.matrixView(
	header: String = "$row x $column Matrix"
): String {
	//每列宽度 = 每列最宽元素的宽度
	val widths = columns.map {
		it.toList()
			.map { num -> format(num).length }
			.max() ?: 0
	}

	//变换一行 : 相邻元素以空格隔开
	fun line(r: Int) =
		(0 until column).joinToString(" ") { c ->
			enlarge(format(get(r, c)), widths[c])
		}

	//构造
	return buildString {
		//矩阵内元素和空格的总宽度
		val inner = widths.sum() + widths.size + 1
		//外部需补充的空白
		val blank = ((header.length - inner - 2) / 2)
			.let { n ->
				if (n > 0) blank(n)
				else "".also { append(blank(-n)) }
			}
		append("$header\n")
		val (pre, fix) = border(row - 1)
		append((0 until row).joinToString("\n") { r ->
			"$blank${pre(r)} ${line(r)} ${fix(r)}"
		})
	}.let {
		//只有一列则用 0 表示 0
		it.takeIf { column > 1 } ?: it.replace('_', '0')
	}
}

//按行切分字符串
private fun String.splitToLines(): List<String> {
	val list = split('\n')
	val width = list.asSequence().map { it.length }.max() ?: 0
	return list.map { "$it${blank(width - it.length)}" }
}

/**
 * 将一系列字符串横向排列
 */
fun tie(vararg items: Any?): String {
	val list = items.map { it.toString().splitToLines() }
	val widths = list.associate { it to it.firstOrNull()?.length }
	val row = list.asSequence().map { it.size }.max() ?: 0
	return (0 until row).joinToString("\n") { r ->
		list.joinToString(" | ") { lines ->
			lines.getOrNull(r) ?: blank(widths[lines] ?: 0)
		}
	}
}

/**
 * 方程组表示为矩阵和向量并列
 */
fun EquationSet.matrixView() = toMatrixForm().matrixView()

/**
 * 方程组表示为矩阵和向量并列
 */
fun EquationSetOfMatrix.matrixView() = tie(args, constants)
