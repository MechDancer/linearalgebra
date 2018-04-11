package equation.solvers

import equation.LinearEquation
import equation.Solver
import matrix.determinant.Determinant
import matrix.determinant.impl.DeterminantImpl
import matrix.toDeterminant
import matrix.toMatrix
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

		val determinantValue = d.value

		if (determinantValue == .0) throw IllegalArgumentException("克莱姆解无法适用于行列式得零的情况")


		return (0 until equation.coefficient.column).fold(mutableListOf<Double>()) { acc, i ->
			acc.add(d.replaceColumn(i, equation.constant.data).value / determinantValue)
			acc
		}.toVector()

	}

	private fun Determinant.replaceColumn(columnIndex: Int, list: List<Double>): Determinant {
		if (list.size != row) throw IllegalArgumentException("新的一列必须和原来的一列行数相等")

		if (columnIndex !in 0..column) throw IllegalArgumentException("列数错误")


		val temp = data.map(kotlin.collections.List<Double>::toMutableList)

		list.forEachIndexed { index, d ->
			temp[index][columnIndex] = d
		}

		return DeterminantImpl(temp.toMatrix())
	}
}