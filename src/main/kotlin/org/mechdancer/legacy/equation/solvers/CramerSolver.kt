package org.mechdancer.legacy.equation.solvers

import org.mechdancer.legacy.equation.LinearEquation
import org.mechdancer.legacy.equation.Solver
import org.mechdancer.legacy.matrix.MatrixElement
import org.mechdancer.legacy.matrix.determinant.Determinant
import org.mechdancer.legacy.matrix.toMatrix
import org.mechdancer.legacy.matrix.transformation.util.impl.operateMatrixDataMutable
import org.mechdancer.legacy.vector.Vector
import org.mechdancer.legacy.vector.impl.VectorImpl
import org.mechdancer.legacy.vector.toVector

object CramerSolver : Solver {

	override fun solve(equation: LinearEquation): Vector {

		if (equation.isHomogeneous)
			if (equation.coefficient.let { it.row >= it.column })
				return VectorImpl(List(equation.coefficient.dimension) { .0 })
			else throw IllegalArgumentException("linear equation is homogeneous," +
					" which has innumerable untrivial solutions")

		if (equation.coefficient.dimension != equation.constant.dimension)
			throw IllegalArgumentException("coefficient dose not equals to constant")
		val d = equation.coefficient.toDeterminant()
		if (d.calculate() == .0) throw IllegalArgumentException("matrix is singular")

		return (0 until equation.coefficient.column).fold(mutableListOf<Double>()) { acc, i ->
			acc.add(d.replaceColumn(i, equation.constant.toList()).calculate() / d.calculate())
			acc
		}.toVector()

	}

	private fun Determinant.replaceColumn(columnIndex: Int, list: MatrixElement): Determinant =
			operateMatrixDataMutable(data) {
				replaceColumn(columnIndex, list)
			}.toMatrix().toDeterminant()

}
