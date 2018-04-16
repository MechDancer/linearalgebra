package equation.solvers

import equation.LinearEquation
import equation.Solver
import matrix.toMatrix
import matrix.transformation.util.impl.ImmutableMatrixDataUtil
import matrix.transformation.util.impl.operateMatrixDataMutable
import vector.Vector
import vector.impl.VectorImpl
import vector.toVector

object CommonSolver : Solver {
	override fun solve(equation: LinearEquation): Vector {
		if (equation.isHomogeneous)
			if (equation.coefficient.let { it.row >= it.column })
				return VectorImpl(List(equation.coefficient.dimension) { .0 })
			else throw IllegalArgumentException("齐次方程组有无限非零解")
		val augmented = operateMatrixDataMutable(equation.coefficient.data) {
			addColumn(equation.constant.data)
		}.toMatrix()
		val s = augmented.rowEchelon()
		return ImmutableMatrixDataUtil.splitColumn(s.data, s.column - 1).toVector()
	}
}