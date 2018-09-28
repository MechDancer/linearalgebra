package org.mechdancer.algebra.equation

import org.mechdancer.algebra.vector.Vector

/**
 * Solver of linearEquation
 * see [LinearEquation]
 */
@FunctionalInterface
interface Solver {
	/**
	 * Solve the equation
	 * @param  equation equation
	 * @return result as a vector
	 */
	fun solve(equation: LinearEquation): Vector
}
