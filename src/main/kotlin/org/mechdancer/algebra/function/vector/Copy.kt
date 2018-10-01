package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.vector.ListVector

/** 复制向量，并进行指定的修改 */
fun Vector.copy(vararg change: Pair<Int, Number>): Vector {
	val map = change.associate { it }
	return List(dim) { map[it]?.toDouble() ?: get(it) }.let(::ListVector)
}

/** 从向量选取指定的维度组成新的向量 */
fun Vector.select(vararg index: Int): Vector =
	toList().filterIndexed { i, _ -> i in index }.let(::ListVector)

/** 从向量选取指定的维度组成新的向量 */
fun Vector.select(range: IntRange): Vector =
	toList().drop(range.first).take(range.last - range.first + 1).let(::ListVector)
