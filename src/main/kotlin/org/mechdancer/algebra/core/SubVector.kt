package org.mechdancer.algebra.core

/**
 * Part of vector
 * 子向量
 */
interface SubVector : Vector {
	/**
	 * The original vector
	 * 源向量
	 * 这个向量应该是不可变的，源变化会导致子向量失效
	 */
	val origin: Vector
}
