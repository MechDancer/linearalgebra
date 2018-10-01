package org.mechdancer.legacy.equation.impl

import org.mechdancer.legacy.equation.LinearEquation
import org.mechdancer.legacy.matrix.Matrix
import org.mechdancer.legacy.vector.Vector
import org.mechdancer.legacy.vector.isZero

data class LinearEquationImpl(
	override val coefficient: Matrix,
	override val constant: Vector)
	: LinearEquation {
	override val isHomogeneous: Boolean = constant.isZero()
}
