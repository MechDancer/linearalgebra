package cn.berberman.algebra.matrix

import cn.berberman.algebra.matrix.impl.MatrixImpl
import cn.berberman.algebra.vector.Vector

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