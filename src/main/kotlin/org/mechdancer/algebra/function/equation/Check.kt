package org.mechdancer.algebra.function.equation

import org.mechdancer.algebra.core.EquationSet
import org.mechdancer.algebra.core.EquationSetOfMatrix
import org.mechdancer.algebra.function.matrix.isSquare

/**
 * 判断方程组是否合理
 * 即 其中至少有一个方程 且 其中各方程参数维数相同
 */
fun EquationSet.isReasonable() =
	map { it.first.dim }.distinct().size == 1

/**
 * 判断方程组是否合理
 * 即 系数矩阵行数 等于 常数向量维数
 */
fun EquationSetOfMatrix.isReasonable() =
	args.row == constants.dim

/**
 * 判断方程组是否欠定
 */
fun EquationSetOfMatrix.isUnderdetermined() =
	args.row < args.column

/**
 * 判断方程组是否适定
 */
fun EquationSetOfMatrix.isWellPosed() =
	args.isSquare()

/**
 * 判断方程组是否超定
 */
fun EquationSetOfMatrix.isOverdetermined() =
	args.row > args.column
