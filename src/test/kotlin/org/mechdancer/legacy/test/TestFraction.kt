package org.mechdancer.legacy.test

import org.junit.Assert
import org.junit.Test
import org.mechdancer.legacy.fraction.toFraction

class TestFraction {
	@Test
	fun testFromString() {
		val a = "-3/3".toFraction()
		val b = "4/-3".toFraction()

		Assert.assertEquals("4/3".toFraction(), a * b)
	}
}
