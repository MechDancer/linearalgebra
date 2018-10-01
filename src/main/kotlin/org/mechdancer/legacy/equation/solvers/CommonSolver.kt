package org.mechdancer.legacy.equation.solvers

import org.mechdancer.legacy.equation.LinearEquation
import org.mechdancer.legacy.equation.Solver
import org.mechdancer.legacy.matrix.toMatrix
import org.mechdancer.legacy.matrix.transformation.util.impl.ImmutableMatrixDataUtil
import org.mechdancer.legacy.matrix.transformation.util.impl.operateMatrixDataMutable
import org.mechdancer.legacy.vector.Vector
import org.mechdancer.legacy.vector.impl.VectorImpl
import org.mechdancer.legacy.vector.toVector

object CommonSolver : Solver {
	override fun solve(equation: LinearEquation): Vector {
		if (equation.isHomogeneous)
			if (equation.coefficient.let { it.row >= it.column })
				return VectorImpl(List(equation.coefficient.dimension) { .0 })
			else throw IllegalArgumentException("linear equation is homogeneous," +
					" which has innumerable untrivial solutions")

		val augmented = operateMatrixDataMutable(equation.coefficient.data) {
			addColumn(equation.constant.toList())
		}.toMatrix()
		val s = augmented.rowEchelon()
		return ImmutableMatrixDataUtil.splitColumn(s.data, s.column - 1).toVector()
	}
}
