package org.mechdancer.algebra

import org.junit.Test
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.core.tie
import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.implement.equation.builder.equations
import org.mechdancer.algebra.implement.matrix.builder.I
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

		val c = I(5)

		println(a)
		println()

		println(tie(a, b, c))
		println()

		val d = equations {
			this[1, 1] = 35
			this[2, 4] = 94
		}

		println(d.matrixView())
		println()

		val e = d.solve()

		println(e)
		println()
	}
}
