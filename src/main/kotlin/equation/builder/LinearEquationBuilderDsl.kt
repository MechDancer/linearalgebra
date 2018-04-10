package equation.builder

import equation.LinearEquation
import equation.impl.LinearEquationImpl
import matrix.Matrix

class LinearEquationBuilderDsl internal constructor() {

	lateinit var coefficient: Matrix
	lateinit var constant: List<Double>

	internal fun build() = LinearEquationImpl(coefficient, constant)
}

fun linearEquation(block: LinearEquationBuilderDsl.() -> Unit):LinearEquation =
		LinearEquationBuilderDsl().apply(block).build()