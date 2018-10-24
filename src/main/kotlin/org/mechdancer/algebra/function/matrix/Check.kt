package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.builder.I


fun Matrix.isSquare() = row == column
fun Matrix.isNotSquare() = row != column

fun Matrix.isDiagonal() =
	filterIndexed { r, c, v -> r != c && v != .0 }.isEmpty()

fun Matrix.isNotDiagonal() = !isDiagonal()

fun Matrix.isFullRank() = row == column && row == rank
fun Matrix.isNotFullRank() = row != column || row != rank

fun Matrix.isSymmetric() =
	row == column &&
		(0 until row - 1).all { r ->
			(0 until r).all { c ->
				get(r, c) == get(c, r)
			}
		}

fun Matrix.isNotSymmetric() = !isSymmetric()

fun Matrix.isOrthogonal() =
	row == column && transpose() * this == I[dim]

fun Matrix.isNotOrthogonal() = !isOrthogonal()
