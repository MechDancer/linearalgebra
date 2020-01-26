package org.mechdancer.geometry.transformation

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.vector.listVectorOf
import org.mechdancer.algebra.implement.vector.to2D
import org.mechdancer.algebra.implement.vector.vector2D

class TestBuildTransformation {
    @Test
    fun testTransformation1() {
        val transformation = mapOf(
            listVectorOf(0, 0) to listVectorOf(+0, +0),
            listVectorOf(4, 2) to listVectorOf(+4, -2),
            listVectorOf(1, 1) to listVectorOf(+1, -1)
        ).toTransformation().also(::println)!!
        Assert.assertTrue(transformation.isChiral)
        Assert.assertEquals(vector2D(4, -2), transformation(vector2D(4, 2)).to2D())
        Assert.assertEquals(vector2D(4, +2), (transformation.inverse())(vector2D(4, -2)))
    }

    @Test
    fun testTransformation2() {
        val transformation = mapOf(
            vector2D(0, 0) to vector2D(0, 0),
            vector2D(1, 0) to vector2D(+0, +1),
            vector2D(0, 1) to vector2D(-1, +0)
        )
            .toTransformationOrthotropic(false)
            .also(::println)!!
        Assert.assertTrue(!transformation.isChiral)
    }

    @Test
    fun testTransformation3() {
        val t0 = mapOf(
            vector2D(0, 0) to vector2D(0, 0),
            vector2D(1, 0) to vector2D(+0, +1),
            vector2D(0, 1) to vector2D(-1, +0)
        ).toTransformation()
        val t1 = mapOf(
            vector2D(0, 0) to vector2D(0, 0),
            vector2D(1, 0) to vector2D(+0, +1),
            vector2D(0, 1) to vector2D(-1, +0)
        ).toTransformationOrthotropic(false)

        println(t0)
        println(t1)
        Assert.assertEquals(t0, t1)

        val t2 = mapOf(
            vector2D(0, 0) to vector2D(0, 0),
            vector2D(1, 0) to vector2D(1, 0),
            vector2D(0, 1) to vector2D(0, -1)
        ).toTransformation()
        val t3 = mapOf(
            vector2D(0, 0) to vector2D(0, 0),
            vector2D(1, 0) to vector2D(1, 0),
            vector2D(0, 1) to vector2D(0, -1)
        ).toTransformationOrthotropic(true)

        Assert.assertEquals(t2, t3)
    }
}
