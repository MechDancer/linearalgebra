package org.mechdancer.algebra.function.equation

import org.mechdancer.algebra.core.EquationSet
import org.mechdancer.algebra.core.EquationSetOfMatrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.function.vector.isNotZero
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
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
 * 尽可能解一个用 <参数 - 常数> 对描述的方程组
 * 对方的参数矩阵直接解算
 */
tailrec fun EquationSetOfMatrix.solve(): Vector? {
	assert(args.row == constants.dim)
	return when {
		args.isSquare()        -> {
			val innerArgs = args.toArrayMatrix()
			val innerConstants = constants.toArrayMatrix()
			innerArgs.simplifyAssignWith(innerConstants)
			innerConstants.column(0).takeIf { innerArgs.lastRow.isNotZero() }
		}
		args.row > args.column ->
			args.transpose()
				.let { EquationSetOfMatrix(it * args, it * constants) }
				.solve()
		else                   ->
			throw RuntimeException("Can't solve Args of ${args.row} x ${args.column}")
	}
}
