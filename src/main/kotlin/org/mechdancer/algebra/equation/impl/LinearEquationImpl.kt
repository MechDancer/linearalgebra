package org.mechdancer.algebra.equation.impl

import org.mechdancer.algebra.equation.LinearEquation
import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.vector.Vector
import org.mechdancer.algebra.vector.isZero

data class LinearEquationImpl(
	override val coefficient: Matrix,
	override val constant: Vector)
	: LinearEquation {
	override val isHomogeneous: Boolean = constant.isZero()
}
