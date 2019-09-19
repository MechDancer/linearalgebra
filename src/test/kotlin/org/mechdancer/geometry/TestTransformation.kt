package org.mechdancer.geometry

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.vector.listVectorOf
import org.mechdancer.algebra.implement.vector.to2D
import org.mechdancer.algebra.implement.vector.vector2DOf
import org.mechdancer.geometry.transformation.toTransformation
import org.mechdancer.geometry.transformation.toTransformationOrthotropic

class TestTransformation {
    @Test
    fun testTransformation1() {
        val transformation = mapOf(
            listVectorOf(0, 0) to listVectorOf(+0, +0),
            listVectorOf(4, 2) to listVectorOf(+4, -2),
            listVectorOf(1, 1) to listVectorOf(+1, -1)
        ).toTransformation().also(::println)!!
        Assert.assertTrue(transformation.isChiral)
        Assert.assertEquals(vector2DOf(4, -2), transformation(vector2DOf(4, 2)).to2D())
        Assert.assertEquals(vector2DOf(4, +2), (-transformation)(vector2DOf(4, -2)))
    }

    @Test
    fun testTransformation2() {
        val transformation = mapOf(
            vector2DOf(0, 0) to vector2DOf(0, 0),
            vector2DOf(1, 0) to vector2DOf(+0, +1),
            vector2DOf(0, 1) to vector2DOf(-1, +0)
        )
            .toTransformationOrthotropic(false)
            .also(::println)!!
        Assert.assertTrue(!transformation.isChiral)
    }

    @Test
    fun testTransformation3() {
        val t0 = mapOf(
            vector2DOf(0, 0) to vector2DOf(0, 0),
            vector2DOf(1, 0) to vector2DOf(+0, +1),
            vector2DOf(0, 1) to vector2DOf(-1, +0)
        ).toTransformation()
        val t1 = mapOf(
            vector2DOf(0, 0) to vector2DOf(0, 0),
            vector2DOf(1, 0) to vector2DOf(+0, +1),
            vector2DOf(0, 1) to vector2DOf(-1, +0)
        ).toTransformationOrthotropic(false)

        println(t0)
        println(t1)
        Assert.assertEquals(t0, t1)

        val t2 = mapOf(
            vector2DOf(0, 0) to vector2DOf(0, 0),
            vector2DOf(1, 0) to vector2DOf(1, 0),
            vector2DOf(0, 1) to vector2DOf(0, -1)
        ).toTransformation()
        val t3 = mapOf(
            vector2DOf(0, 0) to vector2DOf(0, 0),
            vector2DOf(1, 0) to vector2DOf(1, 0),
            vector2DOf(0, 1) to vector2DOf(0, -1)
        ).toTransformationOrthotropic(true)

        Assert.assertEquals(t2, t3)
    }
}
