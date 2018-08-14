package org.mechdancer.algebra.equation.impl

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.vector.Vector

data class LinearEquationImpl(
		override val coefficient: Matrix,
		override val constant: Vector)
	: org.mechdancer.algebra.equation.LinearEquation {
	override val isHomogeneous: Boolean = constant.data.all { it == .0 }
	override fun solve(solver: org.mechdancer.algebra.equation.Solver): Vector = solver.solve(this)
}
