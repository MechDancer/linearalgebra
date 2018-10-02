package org.mechdancer.algebra

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.function.matrix.plus
import org.mechdancer.algebra.implement.matrix.builder.matrix

class TestMatrixCalculate {
	@Test
	fun testMatrixPlus() {
		val m0 = matrix {
			row(1, 2, 3)
			row(3, 2, 1)
		}

		val m1 = matrix {
			row(1, 0, 1)
			row(0, 1, 0)
		}

		val m2 = matrix {
			row(2, 2, 4)
			row(3, 3, 1)
		}

		Assert.assertEquals(m2, m0 + m1)
	}
}
