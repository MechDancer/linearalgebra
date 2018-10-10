package org.mechdancer.algebra.function

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.function.vector.isNotZero
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix

/**
 * 解方程
 * @param arguments 系数矩阵
 * @param constant  常数项
 */
@Suppress("NAME_SHADOWING")
fun solve(arguments: Matrix, constant: Vector): Vector? {
	assert(arguments.dim == constant.dim)
	val arguments = arguments.toArrayMatrix()
	val constant = constant.toArrayMatrix()
	arguments.simplifyAssignWith(constant)
	return constant.column(0).takeIf { arguments.lastRow.isNotZero() }
}

/**
 * 最小二乘解超定方程
 * @param arguments 系数矩阵
 * @param constant  常数项
 */
@Suppress("NAME_SHADOWING")
fun leastSquareSolve(arguments: Matrix, constant: Vector): Vector? {
	assert(arguments.column == constant.dim)
	assert(arguments.row > arguments.column)
	val arguments = (arguments.transpose() * arguments).toArrayMatrix()
	val constant = (arguments.transpose() * constant).toArrayMatrix()
	arguments.simplifyAssignWith(constant)
	return constant.column(0).takeIf { arguments.lastRow.isNotZero() }
}
