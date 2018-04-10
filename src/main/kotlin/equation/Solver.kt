package equation

import matrix.Matrix

@FunctionalInterface
interface Solver {
	fun solve(coefficient: Matrix, constant: List<Double>): List<Double>
}