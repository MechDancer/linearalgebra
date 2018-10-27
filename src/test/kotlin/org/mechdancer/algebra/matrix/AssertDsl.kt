package org.mechdancer.algebra.matrix

import org.junit.Assert
import org.mechdancer.algebra.core.Matrix

inline fun problem(name: String = "", result: Matrix, block: () -> Matrix?) =
	Assert.assertEquals("problem $name is wrong", result, block())

inline fun problem(name: String = "", result: (Matrix?) -> Boolean, block: () -> Matrix?) =
	Assert.assertTrue("problem $name is wrong", result(block()))
