package org.mechdancer.algebra.vector

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.vector.impl.VectorImpl

/** 浮点列表转向量 */
fun MatrixElement.toVector(): Vector = VectorImpl(this)

/** 矩阵转向量 */
fun Matrix.toVector(): Vector = when {
	data.size == 1         -> data[0]
	data.first().size == 1 -> List(data.size) { data[it].first() }

	else                   -> throw IllegalArgumentException("matrix isn't single row or column exclusively")
}.toVector()

/** 从元素构造向量 */
fun vectorOf(vararg double: Double): Vector = VectorImpl(double.toList())
