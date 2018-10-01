package org.mechdancer.legacy.equation.builder

import org.mechdancer.legacy.equation.LinearEquation
import org.mechdancer.legacy.equation.impl.LinearEquationImpl
import org.mechdancer.legacy.matrix.Matrix
import org.mechdancer.legacy.vector.Vector

class LinearEquationBuilderDsl internal constructor() {
	lateinit var coefficient: Matrix

	lateinit var constant: Vector

	internal fun build() = LinearEquationImpl(coefficient, constant)
}

fun linearEquation(block: LinearEquationBuilderDsl.() -> Unit): LinearEquation =
		LinearEquationBuilderDsl().apply(block).build()
