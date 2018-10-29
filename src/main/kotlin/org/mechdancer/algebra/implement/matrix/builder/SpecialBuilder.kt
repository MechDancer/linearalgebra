package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import org.mechdancer.algebra.implement.matrix.special.NumberMatrix
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix

// super shortcut

typealias O = ZeroMatrix
typealias N = NumberMatrix
typealias I = IdentityMatrix

object IdentityMatrix {
	@JvmStatic
	operator fun get(n: Int) = N[n, 1.0]
}

fun Iterable<Number>.toDiagonalMatrix() = DiagonalMatrix(map(Number::toDouble))
fun DoubleArray.toDiagonalMatrix() = DiagonalMatrix(map(Number::toDouble))
