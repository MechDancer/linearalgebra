package org.mechdancer.algebra

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.matrix.builder.matrix

class TestMatrixBuilder {
	@Test
	fun testMatrixBuilder() {
		val a = matrix {
			row(1, 2, 3)
			row(4, 5, 6)
			row(7, 8, 9)
		}

		val b = listMatrixOf(3, 3) { r, c -> 3 * r + c + 1 }

		Assert.assertEquals(b, a)

		val c = matrix {
			column(100, 2, 3)
			column(4, -0.5, 6)
			column(72, 8, 99.99)
		}

		val d = ListMatrix(3, listOf(
			100.0, 4.0, 72.0,
			2.0, -0.5, 8.0,
			3.0, 6.0, 99.99
		))

		Assert.assertEquals(d, c)
	}
}
