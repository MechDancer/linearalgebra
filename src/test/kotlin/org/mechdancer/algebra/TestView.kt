package org.mechdancer.algebra

import org.junit.Test
import org.mechdancer.algebra.core.tie
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOfUnit
import org.mechdancer.algebra.implement.matrix.builder.matrix

class TestView {
	@Test
	fun testView() {
		val a = matrix {
			row(1, 2, 3)
			row(3, 2, 1)
		}

		val b = matrix {
			row(5, 6, 7)
			row(7, 6, 5)
			row(2, 2, 2)
		}

		val c = listMatrixOfUnit(5)

		println(a)
		println()

		println(b)
		println()

		println(c)
		println()

		println(tie(a, b, c))
	}
}
