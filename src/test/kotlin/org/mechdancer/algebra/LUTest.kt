package org.mechdancer.algebra

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.core.tie
import org.mechdancer.algebra.function.matrix.inverse
import org.mechdancer.algebra.function.matrix.rowEchelonAssignWith
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.implement.matrix.builder.*

/**
 * 测试 LU 分解算法
 */
class LUTest {
	@Test
	fun testLU() {
		val a = matrix {
			row(2, 2, 2)
			row(4, 7, 7)
			row(6, 18, 22)
		}

		val temp = a.toArrayMatrix()
		val companion = arrayMatrixOfUnit(3)

		println(" A | I =")
		println(tie(a, companion))

		temp.rowEchelonAssignWith(companion)

		val u = temp.toListMatrix()
		val e = companion.toListMatrix()

		println()
		println("EA | E =")
		println(" U | E =")
		println(tie(u, e))

		val l = e.inverse()
		println("E^-1 =")
		println("   L =")
		println(l)
		Assert.assertEquals(I[3], l * e)

		println("E * U =")
		println(e * u)
		println("meaningless!")

		println("E^-1 * U =")
		println("   L * U =")
		println("       A =")
		Assert.assertEquals(a, l * u.also(::println))
	}
}
