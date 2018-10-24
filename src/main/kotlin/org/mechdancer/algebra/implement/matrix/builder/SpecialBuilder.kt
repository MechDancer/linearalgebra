package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.implement.matrix.special.HilbertMatrix
import org.mechdancer.algebra.implement.matrix.special.NumberMatrix
import org.mechdancer.algebra.implement.matrix.special.VandermondeMatrix
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix

// super shortcut

typealias O = ZeroMatrix
typealias N = NumberMatrix
typealias V = VandermondeMatrix
typealias I = UnitMatrix
typealias H = HilbertMatrix

object UnitMatrix {
	@JvmStatic
	operator fun get(n: Int) = N[n, 1.0]
}
