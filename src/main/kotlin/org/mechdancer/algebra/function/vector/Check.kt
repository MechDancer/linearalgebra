package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector

/**
 * 判断是否零向量
 */
fun Vector.isZero() = toList().all { it == .0 }

/**
 * 判断是否非零向量
 */
fun Vector.isNotZero() = toList().any { it != .0 }

/**
 * 判断是否单位向量
 */
fun Vector.isNormalized() = norm == 1.0

/**
 * 判断是否非单位向量
 */
fun Vector.isNotNormalized() = norm != 1.0

/**
 * 判断是否二维向量
 */
fun Vector.is2D() = dim == 2

/**
 * 判断是否不是二维向量
 */
fun Vector.isNot2D() = dim != 2

/**
 * 判断是否维度比二维更高
 */
fun Vector.isMoreThan2D() = dim > 2
