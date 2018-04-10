package matrix

import matrix.determinant.Determinant
import matrix.determinant.impl.DeterminantImpl
import vector.Vector
import vector.impl.VectorImpl

enum class DefineType { ROW, COLUMN }

typealias MatrixData = List<List<Double>>

fun Matrix.toDeterminant(): Determinant =
		DeterminantImpl(this)


fun Matrix.toVector(): Vector = when {
	data.size == 1         -> data[0]
	data.first().size == 1 -> List(data.size) { data[it].first() }

	else                   -> throw IllegalArgumentException("矩阵非单行或单列")
}.let(::VectorImpl)

