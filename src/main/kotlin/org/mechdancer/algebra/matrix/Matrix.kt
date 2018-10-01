package org.mechdancer.algebra.matrix

import org.mechdancer.algebra.dimensionArgumentError
import org.mechdancer.algebra.matrix.determinant.Determinant
import org.mechdancer.algebra.matrix.transformation.ElementaryTransformation
import org.mechdancer.algebra.vector.Vector

/**
 * Matrix
 * 矩阵
 */
interface Matrix {

	/**
	 * The dim of this matrix
	 * (Only square matrix has)
	 *
	 * 方阵的维数
	 * 试图获取非方阵的维数会产生异常
	 */
	val dimension: Int

	/**
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
	 * Whether it's square
	 */
	val isSquare: Boolean

	/**
	 * The rank of a matrix
	 */
	val rank: Int

	/**
	 * Get an element of this matrix
	 *
	d
	 * @return numerical element
	 */
	operator fun get(row: Int, column: Int): Double

	/**
	 * Add to another matrix
	 *
	 * @param other another matrix
	 * @return result
	 */
	operator fun plus(other: Matrix): Matrix

	/**
	 * Subtract from another matrix
	 *
	 * @param other another matrix
	 * @return result
	 */
	operator fun minus(other: Matrix): Matrix

	/**
	 * Multiply with a scalar
	 *
	 * @param k scalar
	 * @return result
	 */
	operator fun times(k: Double): Matrix

	/**
	 * Multiply with another matrix
	 *
	 * @param other another matrix
	 * @return result
	 */
	operator fun times(other: Matrix): Matrix

	/**
	 * Multiply with a vector
	 *
	 * @param vector vector
	 * @return result
	 */
	operator fun times(vector: Vector): Vector

	/**
	 * Multiply with another matrix
	 * See [times]
	 */
	operator fun invoke(other: Matrix): Matrix

	/**
	 * Multiply with a vector
	 * See [times]
	 */
	operator fun invoke(vector: Vector): Vector

	/**
	 * Divide with another matrix
	 *
	 * @param other another matrix
	 * @return result
	 */
	operator fun div(other: Matrix): Matrix

	/**
	 * Divide with a scalar
	 *
	 * @param k scalar
	 * @return result
	 */
	operator fun div(k: Double): Matrix

	/**
	 * Matrix power
	 *
	 * @param n power
	 */
	infix fun pow(n: Int): Matrix

	/**
	 * To determinant
	 * See [Determinant]
	 */
	fun toDeterminant(): Determinant

	/**
	 * Calculate determinant value
	 * See [Determinant.calculate]
	 */
	fun det(): Double

	/**
	 * Start an elementary transformation
	 * See [ElementaryTransformation]
	 *
	 * @param block dsl action.
	 * @return result
	 */
	fun elementaryTransformation(block: ElementaryTransformation.() -> Unit): Matrix

	/**
	 * Calculate companion matrix
	 *
	 * @return result
	 */
	fun companion(): Matrix

	/**
	 * Transpose this matrix
	 *
	 * @return result
	 */
	fun transpose(): Matrix

	/**
	 * Simplify to echelon matrix
	 *
	 * @return result
	 */
	fun rowEchelon(): Matrix

	/**
	 * Join unit matrix
	 *
	 * @return result
	 */
	fun withUnit(): Matrix

	/**
	 * Calculate inverse matrix by companion matrix
	 *
	 * @return result
	 */
	fun inverseByCompanion(): Matrix

	/**
	 * Calculate inverse matrix by echelon matrix
	 *
	 * @return result
	 */
	fun inverseByRowEchelon(): Matrix

	/**
	 * Clone this matrix
	 *
	 * @return result
	 */
	fun clone(): Matrix

	companion object {

		/**
		 * Construct zero matrix
		 *
		 * @param row The number of rows
		 * @param column The number of columns
		 * @return result
		 */
		fun zeroOf(row: Int, column: Int): Matrix {
			if (row <= 0 || column <= 0) throw IllegalArgumentException("matrix data error")
			return List(row) { List(column) { .0 } }.toMatrix()
		}

		/**
		 * Construct unit matrix
		 *
		 * @param dimension the dim of this matrix
		 * @return result
		 */
		fun unitOf(dimension: Int): Matrix {
			if (dimension <= 0) throw dimensionArgumentError
			return List(dimension) { r ->
				List(dimension) { c ->
					if (c == r) 1.0 else .0
				}
			}.toMatrix()
		}
	}
}
