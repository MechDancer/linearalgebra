package org.mechdancer.algebra.function

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.lastRow
import org.mechdancer.algebra.function.matrix.simplifyAssignWith
import org.mechdancer.algebra.function.vector.isNotZero
import org.mechdancer.algebra.implement.matrix.toArrayMatrix

/**
 * 解方程
 * @param arguments 系数矩阵
 * @param constant  常数项
 */
@Suppress("NAME_SHADOWING")
fun solve(arguments: Matrix, constant: Vector): Vector? {
	assert(arguments.row == constant.dim)
	val arguments = arguments.toArrayMatrix()
	val constant = constant.toArrayMatrix()
	arguments.simplifyAssignWith(constant)
	return constant.column(0).takeIf { arguments.lastRow.isNotZero() }
}
