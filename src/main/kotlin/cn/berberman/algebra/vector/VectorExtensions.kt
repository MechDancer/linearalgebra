package cn.berberman.algebra.vector

import cn.berberman.algebra.matrix.Matrix
import cn.berberman.algebra.matrix.MatrixElement
import cn.berberman.algebra.vector.impl.VectorImpl


fun MatrixElement.toVector(): Vector = VectorImpl(this)

fun Matrix.toVector(): Vector = when {
	data.size == 1         -> data[0]
	data.first().size == 1 -> List(data.size) { data[it].first() }

	else                   -> throw IllegalArgumentException("矩阵非单行或单列")
}.toVector()

fun vectorOf(vararg double: Double): Vector = VectorImpl(double.toList())