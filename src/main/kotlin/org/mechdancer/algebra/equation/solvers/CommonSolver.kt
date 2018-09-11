package org.mechdancer.algebra.equation.solvers

import org.mechdancer.algebra.equation.LinearEquation
import org.mechdancer.algebra.equation.Solver
import org.mechdancer.algebra.matrix.toMatrix
import org.mechdancer.algebra.matrix.transformation.util.impl.ImmutableMatrixDataUtil
import org.mechdancer.algebra.matrix.transformation.util.impl.operateMatrixDataMutable
import org.mechdancer.algebra.vector.Vector
import org.mechdancer.algebra.vector.impl.VectorImpl
import org.mechdancer.algebra.vector.toVector

object CommonSolver : Solver {
	override fun solve(equation: LinearEquation): Vector {
		if (equation.isHomogeneous)
			if (equation.coefficient.let { it.row >= it.column })
				return VectorImpl(List(equation.coefficient.dimension) { .0 })
			else throw IllegalArgumentException("linear equation is homogeneous," +
					" which has innumerable untrivial solutions")

		val augmented = operateMatrixDataMutable(equation.coefficient.data) {
			addColumn(equation.constant.data)
		}.toMatrix()
		val s = augmented.rowEchelon()
		return ImmutableMatrixDataUtil.splitColumn(s.data, s.column - 1).toVector()
	}
}