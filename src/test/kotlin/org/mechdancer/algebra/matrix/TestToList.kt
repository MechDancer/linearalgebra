package org.mechdancer.algebra.matrix

import org.junit.Test
import org.mechdancer.algebra.function.matrix.toList
import org.mechdancer.algebra.implement.matrix.special.ZeroMatrix
import kotlin.test.assertEquals

class TestToList {
    @Test
    fun test() {
        val zero = ZeroMatrix[3, 4]
        assertEquals(DoubleArray(zero.row * zero.column).asList(), zero.toList())
    }
}
