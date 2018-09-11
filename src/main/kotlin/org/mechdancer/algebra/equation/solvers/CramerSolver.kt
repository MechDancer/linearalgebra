package org.mechdancer.algebra.equation.solvers

import org.mechdancer.algebra.equation.LinearEquation
import org.mechdancer.algebra.equation.Solver
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.matrix.determinant.Determinant
import org.mechdancer.algebra.matrix.toMatrix
import org.mechdancer.algebra.matrix.transformation.util.impl.operateMatrixDataMutable
import org.mechdancer.algebra.vector.Vector
import org.mechdancer.algebra.vector.impl.VectorImpl
import org.mechdancer.algebra.vector.toVector

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
			acc.add(d.replaceColumn(i, equation.constant.data).calculate() / d.calculate())
			acc
		}.toVector()

	}

	private fun Determinant.replaceColumn(columnIndex: Int, list: MatrixElement): Determinant =
			operateMatrixDataMutable(data) {
				replaceColumn(columnIndex, list)
			}.toMatrix().toDeterminant()

}