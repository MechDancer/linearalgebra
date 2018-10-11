package org.mechdancer.algebra.implement.equation.builder

import org.mechdancer.algebra.core.EquationSet
import org.mechdancer.algebra.core.EquationSetOfMatrix
import org.mechdancer.algebra.function.equation.isReasonable
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.vector.toListVector

private fun <T, T1, U, U1> Iterable<Pair<T, U>>.splitCollect(
	block1: (List<T>) -> T1,
	block2: (List<U>) -> U1) =
	block1(map { it.first }) to block2(map { it.second })

/**
 * 方程组整理到矩阵形式
 */
fun EquationSet.toMatrixForm(): EquationSetOfMatrix {
	assert(isReasonable())
	val (args, constants) =
		splitCollect(
			{ listMatrixOf(it.size, it.first().dim) { r, c -> it[r][c] } },
			{ it.toListVector() }
		)
	return EquationSetOfMatrix(args, constants)
}

/**
 * 用 Dsl 方式构造方程组
 */
fun equations(block: EquationSetBuilder.() -> Unit): EquationSet =
	EquationSetBuilder().apply(block).equationSet
