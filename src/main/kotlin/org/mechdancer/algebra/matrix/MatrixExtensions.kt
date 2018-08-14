package org.mechdancer.algebra.matrix

import org.mechdancer.algebra.matrix.impl.MatrixImpl
import org.mechdancer.algebra.vector.Vector

typealias MatrixData = List<List<Double>>

typealias MatrixElement = List<Double>

fun Vector.toMatrix(): Matrix =
		List(dimension) { r ->
			List(1) {
				data[r]
			}
		}.toMatrix()

fun MatrixData.toMatrix(): Matrix =
		MatrixImpl(this)