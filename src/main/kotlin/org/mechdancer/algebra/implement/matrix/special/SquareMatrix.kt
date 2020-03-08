package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix

/** 方阵 */
interface SquareMatrix : Matrix {
    val dim: Int
    override val row get() = dim
    override val column get() = dim
}
