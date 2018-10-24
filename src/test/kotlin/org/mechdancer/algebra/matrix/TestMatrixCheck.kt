package org.mechdancer.algebra.matrix

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.function.matrix.isSquare
import org.mechdancer.algebra.function.matrix.isSymmetric
import org.mechdancer.algebra.implement.matrix.builder.I
import org.mechdancer.algebra.implement.matrix.builder.matrix

class TestMatrixCheck {
	@Test
	fun testSquare() {
		I[10].isSquare().let(Assert::assertTrue)
		matrix {
			row(1, 2, 3)
			row(0, 4, 4)
		}.isSquare().let(Assert::assertFalse)
	}

	@Test
	fun testSymmetric() {
		I[10].isSymmetric().let(Assert::assertTrue)
		matrix {
			row(1, 2, 3)
			row(0, 4, 4)
		}.isSymmetric().let(Assert::assertFalse)
		matrix {
			row(1, 2, 3)
			row(0, 4, 4)
			row(0, 0, 4)
		}.isSymmetric().let(Assert::assertFalse)
	}
}
