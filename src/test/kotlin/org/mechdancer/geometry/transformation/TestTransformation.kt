package org.mechdancer.geometry.transformation

import org.junit.Test
import org.mechdancer.algebra.DOUBLE_PRECISION
import org.mechdancer.algebra.implement.vector.vector3D
import kotlin.random.Random
import kotlin.test.assertEquals

class TestTransformation {
    @Test
    fun testPose2D() {
        val a = pose2D(+1, -2, +3)
        val b = pose2D(-3, +2, -1)
        val c = a * b
        assertEquals(pose2D(), a * a.inverse())
        assertEquals(pose2D(), b * b.inverse())

        assertEquals(a, c * b.inverse())
        assertEquals(b, a.inverse() * c)
    }

    @Test
    fun testPose3D() {
        val a = pose3D(+1, -2, +3, -4, +5, -6)
        val b = pose3D(+6, -5, +4, -3, +2, -1)
        val c = a * b
        assertEquals(pose3D(), a * a.inverse())
        assertEquals(pose3D(), b * b.inverse())

        assert(a equivalentWith c * b.inverse())
        assert(b equivalentWith a.inverse() * c)
    }

    @Test
    fun testICP() {
        repeat(10) {
            val pose = Pose3D(vector3D(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()),
                              vector3D(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()))
            val list = List(20) { vector3D(Random.nextDouble(), Random.nextDouble(), Random.nextDouble()) }

            val map = list.map { pose * it to it }
            val expected = pose.toMatrixTransformation()
            assert(expected.equivalentWith(map.toTransformation()!!))
            assert(expected.equivalentWith(map.toTransformationWithSVD(DOUBLE_PRECISION)))
        }
    }
}
