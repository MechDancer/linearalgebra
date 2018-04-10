package equation.solvers

import equation.Solver
import matrix.Matrix
import matrix.determinant.Determinant
import matrix.determinant.impl.DeterminantImpl
import matrix.impl.MatrixImpl
import matrix.toDeterminant

object CramerSolver : Solver {

	override fun solve(coefficient: Matrix, constant: List<Double>): List<Double> {
		val d = coefficient.toDeterminant()
		if (coefficient.row != constant.size) throw IllegalArgumentException("线性方程组系数与常数项数不等")
		if (d.value == .0) throw IllegalArgumentException("线性方程组无解")

		val result = mutableListOf<Double>()
		val dv = d.value

		for (i in 0 until d.column) {
			result.add(d.replaceColumn(i, constant).value / dv)
		}
		return result.toList()
	}

	private fun Determinant.replaceColumn(columnIndex: Int, list: List<Double>): Determinant {
		if (list.size != row) throw IllegalArgumentException("新的一列必须和原来的一列行数相等")

		if (columnIndex !in 0..column) throw IllegalArgumentException("列数错误")


		val temp = data.map(kotlin.collections.List<Double>::toMutableList)

		list.forEachIndexed { index, d ->
			temp[index][columnIndex] = d
		}

		return DeterminantImpl(MatrixImpl(temp))
	}
}