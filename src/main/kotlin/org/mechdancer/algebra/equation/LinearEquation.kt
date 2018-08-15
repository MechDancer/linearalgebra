package org.mechdancer.algebra.equation

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.vector.Vector

interface LinearEquation {

	val coefficient: Matrix

	val constant: Vector

	val isHomogeneous: Boolean

	fun solve(solver: Solver): Vector
}