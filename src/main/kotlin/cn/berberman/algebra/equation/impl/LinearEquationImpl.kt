package cn.berberman.algebra.equation.impl

import cn.berberman.algebra.equation.LinearEquation
import cn.berberman.algebra.equation.Solver
import cn.berberman.algebra.matrix.Matrix
import cn.berberman.algebra.vector.Vector

data class LinearEquationImpl(
		override val coefficient: Matrix,
		override val constant: Vector)
	: LinearEquation {
	override val isHomogeneous: Boolean = constant.data.all { it == .0 }
	override fun solve(solver: Solver): Vector = solver.solve(this)
}
