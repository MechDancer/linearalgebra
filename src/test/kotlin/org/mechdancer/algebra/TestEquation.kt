package org.mechdancer.algebra

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.function.solve
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.vector.listVectorOf

class TestEquation {
	@Test
	fun testEquation() {
		val t = ListMatrix(2, listOf(1.0, 1.0, 2.0, 4.0))
		Assert.assertEquals(listVectorOf(18, 5), solve(t, listVectorOf(23, 56)))
	}
}
