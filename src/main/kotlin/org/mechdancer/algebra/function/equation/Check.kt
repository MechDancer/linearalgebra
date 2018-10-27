package org.mechdancer.algebra.function.equation

import org.mechdancer.algebra.core.AugmentedMatrix
import org.mechdancer.algebra.core.EquationSet
import org.mechdancer.algebra.function.matrix.isSquare
import org.mechdancer.algebra.uniqueOn

/**
 * Check whether a set of equation is correct
 * 判断方程组是否合理
 * 即 其中至少有一个方程 且 其中各方程参数维数相同
 */
fun EquationSet.isAvailable() = uniqueOn { it.first.dim }

/**
 * Check whether a augmented matrix is correct
 * 判断增广矩阵是否合理
 * 即 系数矩阵行数 等于 常数向量维数
 */
fun AugmentedMatrix.isAvailable() = args.row == constants.dim

/**
 * Check whether a equation set is under determined
 * 判断方程组是否欠定
 */
fun AugmentedMatrix.isUnderdetermined() = args.row < args.column

/**
 * Check whether a equation set is well posed
 * 判断方程组是否适定
 */
fun AugmentedMatrix.isWellPosed() = args.isSquare()

/**
 * Check whether a equation set is over determined
 * 判断方程组是否超定
 */
fun AugmentedMatrix.isOverdetermined() = args.row > args.column
