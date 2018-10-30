package org.mechdancer.algebra.function.matrix.decompositions

import Jama.LUDecomposition
import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.function.jama.buildFromJama
import org.mechdancer.algebra.function.jama.toJamaMatrix
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.implement.matrix.builder.matrix

fun Matrix.lu() = LUResult(this)

fun main(args: Array<String>) {
	val matrix = matrix {
		row(1, 2, 3)
		row(0, 0, 5)
		row(0, 0, 4)
	}
	println(buildFromJama(LUDecomposition(matrix.toJamaMatrix()).u))
	val lu = matrix.lu()
	println(lu.u)
	println(lu.d)
	println(lu.l)
	println(lu.u * lu.l)
}
