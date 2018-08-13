package cn.berberman.algebra.equation.builder

import cn.berberman.algebra.equation.LinearEquation
import cn.berberman.algebra.equation.impl.LinearEquationImpl
import cn.berberman.algebra.matrix.Matrix
import cn.berberman.algebra.vector.Vector

class LinearEquationBuilderDsl internal constructor() {
	lateinit var coefficient: Matrix

	lateinit var constant: Vector

	internal fun build() = LinearEquationImpl(coefficient, constant)
}

fun linearEquation(block: LinearEquationBuilderDsl.() -> Unit): LinearEquation =
		LinearEquationBuilderDsl().apply(block).build()
