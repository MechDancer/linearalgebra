package org.mechdancer.algebra.matrix.determinant

import org.mechdancer.algebra.matrix.MatrixData

interface Determinant {

	/**
	 * Determinant data
	 * See [MatrixData]
	 */
	val data: MatrixData

	/**
	 * The number of rows
	 */
	val row: Int

	/**
	 * The number of columns
	 */
	val column: Int

	/**
	 * The dimension of this determinant
	 */
	val dimension: Int

	/**
	 * Calculate this determinant value
	 *
	 * @return det value
	 */
	fun calculate(): Double

	/**
	 * Get cofactor of this determinant
	 * Cofactor is this determinant cut down from itself
	 * by removing one of its rows or columns
	 *
	 * @param row row to remove
	 * @param column column to remove
	 * @return result
	 */
	fun getCofactor(row: Int, column: Int): Determinant

	/**
	 * Get algebra cofactor of this determinant
	 * get cofactor first and add corresponding symbol
	 * See [getCofactor]
	 *
	 * @param row row number
	 * @param column column number
	 * @return value
	 */
	fun getAlgebraCofactor(row: Int, column: Int): Double

}
