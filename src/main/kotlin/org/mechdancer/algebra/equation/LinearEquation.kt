package org.mechdancer.algebra.equation

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.vector.Vector

/**
 * LinearEquation
 */
interface LinearEquation {

	/**
	 * Coefficient of this equation
	 * represents by a matrix
	 */
	val coefficient: Matrix

	/**
	 * Constant of this equation
	 * represents by a vector
	 */
	val constant: Vector

	/**
	 * Whether it's homogeneous
	 */
	val isHomogeneous: Boolean

	/**
	 * Solve this equation
	 * See [Solver.solve]
	 *
	 * @param solver solver
	 * @return result
	 */
	fun solve(solver: Solver): Vector
}