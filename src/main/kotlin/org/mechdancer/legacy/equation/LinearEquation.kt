package org.mechdancer.legacy.equation

import org.mechdancer.legacy.matrix.Matrix
import org.mechdancer.legacy.vector.Vector

/**
 * LinearEquation
 * 线性方程组，即其中所有未知数指数为1的方程组
 */
interface LinearEquation {

	/**
	 * Coefficient of this equation
	 * represents by a matrix
	 * 系数矩阵
	 */
	val coefficient: Matrix

	/**
	 * Constant of this equation
	 * represents by a vector
	 * 常数项
	 */
	val constant: Vector

	/**
	 * Whether it's homogeneous
	 * 齐次性，常数项为零向量称作齐次
	 */
	val isHomogeneous: Boolean
}
