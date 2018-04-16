package equation.solvers

import equation.LinearEquation
import equation.Solver
import matrix.MatrixElement
import matrix.determinant.Determinant
import matrix.toMatrix
import matrix.transformation.util.impl.operateMatrixDataMutable
import vector.Vector
import vector.impl.VectorImpl
import vector.toVector

object CramerSolver : Solver {

	override fun solve(equation: LinearEquation): Vector {

		if (equation.isHomogeneous)
			if (equation.coefficient.let { it.row >= it.column })
				return VectorImpl(List(equation.coefficient.dimension) { .0 })
			else throw IllegalArgumentException("齐次方程组有无限非零解")

		if (equation.coefficient.dimension != equation.constant.dimension)
			throw IllegalArgumentException("克莱姆解无法适用方程个数与未知数个数不同的情况")
		val d = equation.coefficient.toDeterminant()


		if (d.calculate() == .0) throw IllegalArgumentException("克莱姆解无法适用于行列式得零的情况")


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