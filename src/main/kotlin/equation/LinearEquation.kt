package equation

import matrix.Matrix
import vector.Vector

interface LinearEquation {

	val coefficient: Matrix

	val constant: Vector

	val isHomogeneous: Boolean

	fun solve(solver: Solver): Vector
}