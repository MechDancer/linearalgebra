package equation

import matrix.Matrix

interface LinearEquation {

	val coefficient: Matrix

	val constant: List<Double>

	fun solve(solver: Solver): List<Double>
}