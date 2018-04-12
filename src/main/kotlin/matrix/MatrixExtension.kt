package matrix

import matrix.builder.Row
import matrix.builder.matrix
import matrix.impl.MatrixImpl
import vector.Vector

typealias MatrixData = List<List<Double>>

typealias MatrixElement = List<Double>

fun Vector.toMatrix(): Matrix =
		MatrixImpl(listOf(data))

fun MatrixData.toMatrix(): Matrix =
		MatrixImpl(this)
