package org.mechdancer.algebra.implement.equation.builder

import org.mechdancer.algebra.core.Equation
import org.mechdancer.algebra.implement.vector.toListVector

/**
 * 方程组构造工具
 */
class EquationSetBuilder {
	private val equations = mutableSetOf<Equation>()

	val equationSet = object : Set<Equation> by equations {}

	operator fun set(vararg args: Number, value: Number) {
		equations += Equation(args.map { it.toDouble() }.toListVector(), value)
	}
}
