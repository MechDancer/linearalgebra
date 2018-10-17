package org.mechdancer.algebra.matrix

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.*

class TestMatrixEquals {
	@Test
	fun testMatrixEquals() {
		val a = matrix {
			row(1, 2, 3)
			row(3, 2, 1)
		}

		val b = matrix {
			column(1, 3)
			column(2, 2)
			column(3, 1)
		}

		val c = ListMatrix(3, listOf(1.0, 2.0, 3.0, 3.0, 2.0, 1.0))

		Assert.assertEquals(c, a)
		Assert.assertEquals(c, b)
		Assert.assertEquals(a, b)
	}

	@Test
	fun testMatrixTypeEquals() {
		val a = matrix {
			row(1, 2, 3)
			row(3, 2, 1)
		}

		Assert.assertEquals(a, a.toArrayMatrix())
	}

	@Test
	fun testZeroMatrixEquals() {
		val a = matrix {
			row(0, 0, 0)
			row(0, 0, 0)
		}

		val b = arrayMatrixOfZero(2, 3)

		val c = O(2, 3)

		Assert.assertEquals(a, b)
		Assert.assertEquals(a, c)
	}

	@Test
	fun testUnitMatrixEquals() {
		val a = matrix {
			row(1, 0, 0)
			row(0, 1, 0)
			row(0, 0, 1)
		}

		val b = listMatrixOfUnit(3)

		val c = I(3)

		Assert.assertEquals(a, b)
		Assert.assertEquals(a, c)
	}

	@Test
	fun testNumberMatrixEquals() {
		val a = matrix {
			row(.3, 0, 0)
			row(0, .3, 0)
			row(0, 0, .3)
		}

		val b = .3 toListMatrix 3

		val c = N(3, .3)

		Assert.assertEquals(a, b)
		Assert.assertEquals(a, c)
	}
}
