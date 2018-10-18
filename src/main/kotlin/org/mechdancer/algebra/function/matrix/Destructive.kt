package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.ValueMutableMatrix
import org.mechdancer.algebra.function.vector.isNotZero
import org.mechdancer.algebra.function.vector.isZero
import org.mechdancer.algebra.implement.matrix.builder.arrayMatrixOfUnit

/**
 * 计算矩阵的秩，将破坏原矩阵
 */
fun ValueMutableMatrix.rankDestructive() =
	rowEchelonAssign().rows.dropLastWhile { it.isZero() }.size

/**
 * 计算矩阵的逆，将破坏原矩阵
 */
fun ValueMutableMatrix.inverseDestructive() = run {
	val u = arrayMatrixOfUnit(dim)
	simplifyAssignWith(u)
	if (lastRow.isNotZero()) u else null
}
