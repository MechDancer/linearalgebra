package org.mechdancer.algebra.vector

import org.junit.Test
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.vector.dot
import org.mechdancer.algebra.function.vector.isNormalized
import org.mechdancer.algebra.function.vector.orthogonalize
import org.mechdancer.algebra.implement.vector.vector3D

class TestOrthogonalize {
    @Test
    fun testOrthogonalize() {
        val orthogonalized =
            listOf(vector3D(1, 0, 0),
                   vector3D(1, 2, 3),
                   vector3D(5, 6, 7)
            ).orthogonalize()
        orthogonalized.forEachIndexed { i, v ->
            assert(v.isNormalized())
            for (j in 0 until i) assert(doubleEquals(.0, v dot orthogonalized[j]))
        }
    }
}
