package org.mechdancer.algebra.function.equation

import org.mechdancer.algebra.core.EquationSetOfMatrix

/**
 * 获取系数矩阵
 */
val EquationSetOfMatrix.args get() = first

/**
 * 获取常数项
 */
val EquationSetOfMatrix.constants get() = second
