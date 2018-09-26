package org.mechdancer.algebra.test

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.matrix.builder.Row
import org.mechdancer.algebra.matrix.builder.matrix

class TestMatrix {

	@Test
	fun testAdd() {
		val matrix = matrix {
			Row[1.0, 2.0]
			Row[2.0, 1.0]
		}
		val matrix2 = matrix {
			Row[3.0, 2.0]
			Row[6.0, 1.0]
		}

		Assert.assertEquals(matrix {
			Row[4.0, 4.0]
			Row[8.0, 2.0]
		}, matrix + matrix2)
	}

	@Test
	fun testInverse() {
		val m = matrix {
			Row[1.0, 2.0, 3.0]
			Row[2.0, 1.0, 3.0]
			Row[5.0, 8.0, 3.0]
		}
		val expect = matrix {
			Row[-0.7, 0.6, 0.1]
			Row[0.3, -0.4, 0.1]
			Row[11.0 / 30.0, 1.0 / 15.0, -0.1]
		}
		println(m.rowEchelon())
		println(m.withUnit().rowEchelon())
		Assert.assertEquals(expect, m.inverseByCompanion())
	}
}