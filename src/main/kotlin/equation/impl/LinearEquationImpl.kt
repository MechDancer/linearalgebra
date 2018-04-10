package equation.impl

import equation.LinearEquation
import equation.Solver
import matrix.Matrix

data class LinearEquationImpl(override val coefficient: Matrix, override val constant: List<Double>) : LinearEquation {
	override fun solve(solver: Solver): List<Double> = solver.solve(coefficient, constant)
}