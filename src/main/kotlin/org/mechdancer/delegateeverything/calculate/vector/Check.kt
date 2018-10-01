package org.mechdancer.delegateeverything.calculate.vector

import org.mechdancer.delegateeverything.core.Vector

/** 判断是否零向量 */
fun Vector.isZero() = toList().all { it == .0 }

/** 判断是否非零向量 */
fun Vector.isNotZero() = toList().any { it != .0 }

/** 判断是否单位向量 */
fun Vector.isNormalized() = norm == 1.0

/** 判断是否非单位向量 */
fun Vector.isNotNormalized() = norm != 1.0
