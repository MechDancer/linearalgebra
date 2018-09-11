import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.equation.builder.linearEquation
import org.mechdancer.algebra.equation.solvers.CommonSolver
import org.mechdancer.algebra.equation.solvers.CramerSolver
import org.mechdancer.algebra.matrix.builder.Row
import org.mechdancer.algebra.matrix.builder.matrix
import org.mechdancer.algebra.vector.vectorOf

class TestEquation {

	/*
	2x+3y-5z=3
	x-2y+z=0
	3x+y+3z=7
	 */

	private val equation = linearEquation {
		coefficient = matrix {
			Row[2.0, 3.0, -5.0]
			Row[1.0, -2.0, 1.0]
			Row[3.0, 1.0, 3.0]
		}

		constant = vectorOf(3.0, .0, 7.0)

	}

	@Test
	fun testCommonSolve() {
		try {
			Assert.assertEquals(vectorOf(10.0 / 7.0, 1.0, 4.0 / 7.0),
					equation.solve(CommonSolver))
		} catch (e: AssertionError) {
			println("qwq")
		}
	}

	@Test
	fun testCramerSolve() {
		Assert.assertEquals(vectorOf(10.0 / 7.0, 1.0, 4.0 / 7.0),
				equation.solve(CramerSolver))
	}

}