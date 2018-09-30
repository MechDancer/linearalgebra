package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.ValueMutableMatrix
import org.mechdancer.delegateeverything.implement.vector.isNotZero

/**
 * 计算矩阵的秩，将破坏原矩阵
 */
fun ValueMutableMatrix.getRankDestructive() =
	rowEchelonAssign().rows.sumBy { if (it.isNotZero()) 1 else 0 }
