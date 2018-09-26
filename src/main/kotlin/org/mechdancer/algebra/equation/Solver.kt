package org.mechdancer.algebra.equation

import org.mechdancer.algebra.vector.Vector

@FunctionalInterface
/**
 * Solver of linearEquation
 * see [LinearEquation]
 */
interface Solver {
	/**
	 * Solve the equation
	 *
	 * @param equation equation
	 * @return result as a vector
	 */
	fun solve(equation: LinearEquation): Vector
}