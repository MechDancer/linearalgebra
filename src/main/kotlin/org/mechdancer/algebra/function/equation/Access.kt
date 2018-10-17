package org.mechdancer.algebra.function.equation

import org.mechdancer.algebra.core.AugmentedMatrix

/**
 * Get the argument matrix
 * 获取系数矩阵
 */
val AugmentedMatrix.args get() = first

/**
 * Get the constant vector
 * 获取常数项
 */
val AugmentedMatrix.constants get() = second
