package org.mechdancer.algebra.vector

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.vector.impl.VectorImpl

/**
 * Convert a MatrixElement into a Vector
 * See [MatrixElement]
 * @receiver MatrixElement
 * @return result
 */
fun MatrixElement.toVector(): Vector = VectorImpl(this)

/**
 * Convert a matrix into a Vector
 * Matrix must be either single column or single row.
 *
 * @receiver matrix
 * @return result
 */
fun Matrix.toVector(): Vector = when {
	data.size == 1         -> data[0]
	data.first().size == 1 -> List(data.size) { data[it].first() }

	else                   -> throw IllegalArgumentException("matrix isn't single row or column exclusively")
}.toVector()

/**
 * Build a vector from doubles
 *
 * @param double doubles
 */
fun vectorOf(vararg double: Double): Vector = VectorImpl(double.toList())
