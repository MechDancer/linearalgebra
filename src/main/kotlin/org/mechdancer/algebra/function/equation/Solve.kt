package org.mechdancer.algebra.function.equation

import org.mechdancer.algebra.core.EquationSet
import org.mechdancer.algebra.core.EquationSetOfMatrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.lastRow
import org.mechdancer.algebra.function.matrix.simplifyAssignWith
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.matrix.transpose
import org.mechdancer.algebra.function.vector.isNotZero
import org.mechdancer.algebra.implement.equation.builder.toMatrixForm
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix

/**
 * 尽可能解一个用 <参数 - 常数> 对描述的方程组
 */
fun EquationSet.solve() = toMatrixForm().solve()

/**
 * 尽可能解一个用系数矩阵和常数向量描述的方程组
 */
tailrec fun EquationSetOfMatrix.solve(): Vector? {
	assert(isReasonable())
	return when {
		//适定，直接用高斯消元解
		isWellPosed()      -> {
			val innerArgs = args.toArrayMatrix()
			val innerConstants = constants.toArrayMatrix()
			innerArgs.simplifyAssignWith(innerConstants)
			innerConstants.column(0).takeIf { innerArgs.lastRow.isNotZero() }
		}
		//超定，变为适定再解
		isOverdetermined() ->
			// 左乘转置，将超定变为适定
			// ┌   ┐ ┌   ┐┌   ┐    ┌   ┐T┌   ┐ ┌   ┐T┌   ┐┌   ┐
			// │ y │=│ A ││ x │ => │ A │ │ y │=│ A │ │ A ││ x │
			// └   ┘ └   ┘└   ┘    └   ┘ └   ┘ └   ┘ └   ┘└   ┘
			args.transpose()
				.let { EquationSetOfMatrix(it * args, it * constants) }
				.solve()
		//欠定，解为超平面，暂时无法解
		else               ->
			TODO("Can't solve args in ${args.row} x ${args.column} matrix recently")
	}
}
