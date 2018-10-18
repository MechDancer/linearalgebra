package org.mechdancer.geometry

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.vector.listVectorOf
import org.mechdancer.algebra.implement.vector.vector2DOf
import org.mechdancer.geometry.position.toTransformation

class TestTransformation {
	@Test
	fun testTransformation1() {
		val transformation = mapOf(
			listVectorOf(0, 0) to listVectorOf(+0, +0),
			listVectorOf(4, 2) to listVectorOf(+4, -2),
			listVectorOf(1, 1) to listVectorOf(+1, -1)
		).toTransformation()
		Assert.assertTrue(transformation?.isChiral() == true)
	}

	@Test
	fun testTransformation2() {
		val transformation = mapOf(
			vector2DOf(0, 0) to vector2DOf(-0, +0),
			vector2DOf(4, 2) to vector2DOf(-4, -2),
			vector2DOf(1, 1) to vector2DOf(-1, -1)
		).toTransformation()
		Assert.assertTrue(transformation?.isChiral() == false)
	}
}
