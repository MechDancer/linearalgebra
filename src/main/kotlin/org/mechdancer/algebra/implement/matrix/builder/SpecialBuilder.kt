package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.implement.matrix.special.NumberMatrix
import org.mechdancer.algebra.implement.matrix.special.VandermondeMatrix
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix

// super shortcut

typealias O = ZeroMatrix
typealias N = NumberMatrix
typealias I = UnitMatrix

fun vandermonde(vararg elements: Number) =
	VandermondeMatrix[elements.toList()]

fun Iterable<Number>.toVandermonde() = VandermondeMatrix[this]
fun DoubleArray.toVandermonde() = VandermondeMatrix[this.toList()]

object UnitMatrix {
	@JvmStatic
	operator fun get(n: Int) = N[n, 1.0]
}
