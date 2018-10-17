package org.mechdancer.algebra.implement.equation.builder

import org.mechdancer.algebra.core.AugmentedMatrix
import org.mechdancer.algebra.core.EquationSet
import org.mechdancer.algebra.function.equation.isAvailable
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.vector.toListVector

private fun <T, T1, U, U1> Iterable<Pair<T, U>>.splitCollect(
	block1: (List<T>) -> T1,
	block2: (List<U>) -> U1) =
	block1(map { it.first }) to block2(map { it.second })

/**
 * 方程组整理到矩阵形式
 */
fun EquationSet.toMatrixForm(): AugmentedMatrix {
	assert(isAvailable())
	val (args, constants) =
		splitCollect(
			{ listMatrixOf(it.size, it.first().dim) { r, c -> it[r][c] } },
			{ it.toListVector() }
		)
	return AugmentedMatrix(args, constants)
}

/**
 * 用 Dsl 方式构造方程组
 */
fun equations(block: EquationSetBuilder.() -> Unit): EquationSet =
	EquationSetBuilder().apply(block).equationSet
