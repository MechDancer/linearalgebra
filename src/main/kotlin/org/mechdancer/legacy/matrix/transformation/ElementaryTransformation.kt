package org.mechdancer.legacy.matrix.transformation

import org.mechdancer.legacy.matrix.Matrix
import org.mechdancer.legacy.matrix.transformation.util.MatrixDataUtil

/**
 * Actions of elementary transformation
 */
interface ElementaryTransformation {

	/**
	 * Temporary matrix data
	 * Transformation is implemented by data util.
	 * See [MatrixDataUtil]
	 */
	val dataUtil: MatrixDataUtil

	/**
	 * One row multiply with a scalar
	 *
	 * @param row row number
	 * @param k scalar
	 */
	fun rowMultiply(row: Int, k: Double)

	/**
	 * Swap two rows
	 *
	 * @param row1 the first row number
	 * @param row2 the second row number
	 */
	fun rowSwap(row1: Int, row2: Int)

	/**
	 * Add a row into another row and  multiply with a scalar
	 *
	 * @param from row number
	 * @param to row number
	 * @param k scalar
	 */
	fun rowAddTo(from: Int, to: Int, k: Double)

	/**
	 * One column multiply with a scalar
	 *
	 * @param column column number
	 * @param k scalar
	 */
	fun columnMultiply(column: Int, k: Double)

	/**
	 * Swap two columns
	 *
	 * @param column1 the first column number
	 * @param column2 the second column number
	 */
	fun columnSwap(column1: Int, column2: Int)

	/**
	 * Add a column into another column and  multiply with a scalar
	 *
	 * @param from column number
	 * @param to column number
	 * @param k scalar
	 */
	fun columnAddTo(from: Int, to: Int, k: Double)

	/**
	 * Get the final result
	 * Generate from [MatrixDataUtil]
	 *
	 * @return result
	 */
	fun getResult(): Matrix

}
