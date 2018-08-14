package org.mechdancer.algebra.equation.builder

import org.mechdancer.algebra.equation.impl.LinearEquationImpl
import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.vector.Vector

class LinearEquationBuilderDsl internal constructor() {
	lateinit var coefficient: Matrix

	lateinit var constant: Vector

	internal fun build() = LinearEquationImpl(coefficient, constant)
}

fun linearEquation(block: LinearEquationBuilderDsl.() -> Unit): org.mechdancer.algebra.equation.LinearEquation =
		LinearEquationBuilderDsl().apply(block).build()
