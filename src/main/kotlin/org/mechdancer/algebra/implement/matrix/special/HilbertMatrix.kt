package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf

object HilbertMatrix {
	operator fun get(m: Int, n: Int) =
		listMatrixOf(m, n) { r, c -> 1.0 / (r + c + 1) }

	operator fun get(d: Int) = get(d, d)
}
