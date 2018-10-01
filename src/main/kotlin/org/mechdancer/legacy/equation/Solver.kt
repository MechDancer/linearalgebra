package org.mechdancer.legacy.equation

import org.mechdancer.legacy.vector.Vector

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
