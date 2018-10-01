package org.mechdancer.delegateeverything.calculate.matrix

import org.mechdancer.delegateeverything.calculate.vector.isNotZero
import org.mechdancer.delegateeverything.core.ValueMutableMatrix
import org.mechdancer.delegateeverything.implement.matrix.arrayMatrixOfUnit

/**
 * 计算矩阵的秩，将破坏原矩阵
 */
fun ValueMutableMatrix.getRankDestructive() =
	rowEchelonAssign().rows.sumBy { if (it.isNotZero()) 1 else 0 }

/**
 * 计算矩阵的逆，将破坏原矩阵
 */
fun ValueMutableMatrix.inverseDestructive() = run {
	val u = arrayMatrixOfUnit(dim)
	simplifyAssignWith(u)
	if (row(row - 1).isNotZero()) u else null
}
