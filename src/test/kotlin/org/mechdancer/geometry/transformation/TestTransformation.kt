package org.mechdancer.geometry.transformation

import org.junit.Test
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
        val a = pose3D(+1, -2, +3, -4/*, +5, -6*/)
        val b = pose3D(+6, -5, +4/*, -3, +2, -1*/)
        val c = a * b
        assertEquals(pose3D(), a * a.inverse())
        assertEquals(pose3D(), b * b.inverse())

        assert(a equivalentWith c * b.inverse())
        assert(b equivalentWith a.inverse() * c)
    }
}
