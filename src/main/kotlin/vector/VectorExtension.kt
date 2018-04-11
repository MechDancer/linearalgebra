package vector

import matrix.Matrix
import vector.impl.VectorImpl


fun List<Double>.toVector() = VectorImpl(this)

fun Matrix.toVector(): Vector = when {
	data.size == 1         -> data[0]
	data.first().size == 1 -> List(data.size) { data[it].first() }

	else                   -> throw IllegalArgumentException("矩阵非单行或单列")
}.let(::VectorImpl)
