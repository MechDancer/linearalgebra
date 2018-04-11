package equation

import vector.Vector

@FunctionalInterface
interface Solver {
	fun solve(equation: LinearEquation): Vector
}