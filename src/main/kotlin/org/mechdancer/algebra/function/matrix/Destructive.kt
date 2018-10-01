package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.ValueMutableMatrix
import org.mechdancer.algebra.function.vector.isNotZero
import org.mechdancer.algebra.implement.matrix.arrayMatrixOfUnit

/**
 * 计算矩阵的秩，将破坏原矩阵
 */
fun ValueMutableMatrix.rankDestructive() =
	rowEchelonAssign().rows.sumBy { if (it.isNotZero()) 1 else 0 }

/**
 * 计算矩阵的逆，将破坏原矩阵
 */
fun ValueMutableMatrix.inverseDestructive() = run {
	val u = arrayMatrixOfUnit(dim)
	simplifyAssignWith(u)
	if (row(row - 1).isNotZero()) u else null
}
