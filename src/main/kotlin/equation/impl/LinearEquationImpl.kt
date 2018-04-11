package equation.impl

import equation.LinearEquation
import equation.Solver
import matrix.Matrix
import vector.Vector

data class LinearEquationImpl(override val coefficient: Matrix, override val constant: Vector) : LinearEquation {

	override val isHomogeneous: Boolean = constant.data.all { it == .0 }

	override fun solve(solver: Solver): Vector = solver.solve(this)

}