package matrix

import matrix.impl.MatrixImpl
import vector.Vector

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