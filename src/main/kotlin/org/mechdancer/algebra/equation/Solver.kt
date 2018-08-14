package org.mechdancer.algebra.equation

import org.mechdancer.algebra.vector.Vector

@FunctionalInterface
interface Solver {
	fun solve(equation: org.mechdancer.algebra.equation.LinearEquation): Vector
}