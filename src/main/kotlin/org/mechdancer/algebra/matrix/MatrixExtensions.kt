package org.mechdancer.algebra.matrix

import org.mechdancer.algebra.matrix.impl.MatrixImpl
import org.mechdancer.algebra.vector.Vector

/**
 * MatrixData
 * A list of MatrixElements, represents rows of a matrix.
 * Every `List<MatrixElement>` , i.e.`List<List<Double>>` can be counted as a MatrixData.
 */
typealias MatrixData = List<MatrixElement>

/**
 * MatrixElement
 * A list of doubles, as one row of a matrix or a vector.
 * Every `List<Double>` can be counted as a MatrixElement.
 */
typealias MatrixElement = List<Double>

/**
 * Convert a vector into matrix
 *
 * @receiver vector
 * @return result
 */
fun Vector.toMatrix(): Matrix =
	List(dimension) { r -> List(1) { toList()[r] } }.toMatrix()

/**
 * Use MatrixData to build a matrix
 *
 * @receiver matrix data
 * @return result
 */
fun MatrixData.toMatrix(): Matrix =
		MatrixImpl(this)
