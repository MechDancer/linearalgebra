package org.mechdancer.geometry

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.vector.listVectorOf
import org.mechdancer.algebra.implement.vector.vector2DOf
import org.mechdancer.geometry.transformation.toTransformation
import org.mechdancer.geometry.transformation.toTransformation2D
import org.mechdancer.geometry.transformation.toTransformation2DChiral

class TestTransformation {
	@Test
	fun testTransformation1() {
		val transformation = mapOf(
			listVectorOf(0, 0) to listVectorOf(+0, +0),
			listVectorOf(4, 2) to listVectorOf(+4, -2),
			listVectorOf(1, 1) to listVectorOf(+1, -1)
		).toTransformation().also(::println)
		Assert.assertTrue(transformation?.isChiral() == true)
		Assert.assertEquals(
			vector2DOf(4, -2),
			transformation
				?.invoke(vector2DOf(4, 2))
		)
		Assert.assertEquals(
			vector2DOf(4, 2),
			transformation
				?.reversed
				?.invoke(vector2DOf(4, -2))
		)
	}

	@Test
	fun testTransformation2() {
		val transformation = mapOf(
			vector2DOf(0, 0) to vector2DOf(0, 0),
			vector2DOf(1, 0) to vector2DOf(+0, +1),
			vector2DOf(0, 1) to vector2DOf(-1, +0)
		).toTransformation2D().also(::println)
		Assert.assertTrue(transformation?.isChiral() == false)
	}

	@Test
	fun testTransformation3() {
		mapOf(
			vector2DOf(0, 0) to vector2DOf(0, 0),
			vector2DOf(1, 0) to vector2DOf(+0, +1),
			vector2DOf(0, 1) to vector2DOf(-1, +0)
		).toTransformation().also(::println)
		mapOf(
			vector2DOf(0, 0) to vector2DOf(0, 0),
			vector2DOf(1, 0) to vector2DOf(+0, +1),
			vector2DOf(0, 1) to vector2DOf(-1, +0)
		).toTransformation2D().also(::println)

		mapOf(
			vector2DOf(0, 0) to vector2DOf(0, 0),
			vector2DOf(1, 0) to vector2DOf(1, 0),
			vector2DOf(0, 1) to vector2DOf(0, -1)
		).toTransformation().also(::println)
		mapOf(
			vector2DOf(0, 0) to vector2DOf(0, 0),
			vector2DOf(1, 0) to vector2DOf(1, 0),
			vector2DOf(0, 1) to vector2DOf(0, -1)
		).toTransformation2DChiral().also(::println)
	}
}
