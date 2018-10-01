package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix

val Matrix.dim get() = if (row == column) row else -1

fun Matrix.isSquare() = row == column
fun Matrix.isNotSquare() = !isSquare()

fun Matrix.isFullRank() = isSquare() && row == rank
fun Matrix.isNotFullRank() = !isFullRank()
