package org.mechdancer.algebra.core

/**
 * Part of matrix
 * 子矩阵
 */
interface SubMatrix : Matrix {
	/**
	 * The original matrix
	 * 源矩阵
	 *
	 * Make sure this matrix is immutable
	 *     origin changing cause sub-matrix invalid
	 * 这个矩阵应该是不可变的，源变化会导致子矩阵失效
	 */
	val origin: Matrix
}
