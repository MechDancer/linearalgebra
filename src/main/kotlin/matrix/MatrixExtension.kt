package matrix

import matrix.determinant.Determinant
import matrix.determinant.impl.DeterminantImpl
import matrix.impl.MatrixImpl
import vector.Vector

enum class DefineType { ROW, COLUMN }

typealias MatrixData = List<List<Double>>

fun Matrix.toDeterminant(): Determinant =
		DeterminantImpl(this)


fun Vector.toMatrix(): Matrix =
		MatrixImpl(DefineType.COLUMN, listOf(data))

fun MatrixData.toMatrix(defineType: DefineType = DefineType.ROW): Matrix =
		MatrixImpl(defineType, this)