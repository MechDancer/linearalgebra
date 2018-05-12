package cn.berberman.algebra.equation.solvers

import cn.berberman.algebra.equation.LinearEquation
import cn.berberman.algebra.equation.Solver
import cn.berberman.algebra.matrix.toMatrix
import cn.berberman.algebra.matrix.transformation.util.impl.ImmutableMatrixDataUtil
import cn.berberman.algebra.matrix.transformation.util.impl.operateMatrixDataMutable
import cn.berberman.algebra.vector.Vector
import cn.berberman.algebra.vector.impl.VectorImpl
import cn.berberman.algebra.vector.toVector

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