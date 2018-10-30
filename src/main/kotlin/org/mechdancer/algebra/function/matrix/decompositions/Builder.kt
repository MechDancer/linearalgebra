package org.mechdancer.algebra.function.matrix.decompositions

import Jama.LUDecomposition
import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.function.jama.buildFromJama
import org.mechdancer.algebra.function.jama.toJamaMatrix
import org.mechdancer.algebra.function.matrix.rowEchelon
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.matrix.builder.random
import kotlin.system.measureTimeMillis

fun Matrix.lu() = LUResult(this)

object Test1 {
	@JvmStatic
	fun main(args: Array<String>) {
		val matrix = matrix {
			row(2, 1, 1, 1)
			row(1, 2, 1, 1)
			row(1, 100, 2, 1)
			row(1, 1, 1, 2)
		}
		val luJama = LUDecomposition(matrix.toJamaMatrix())
		println(buildFromJama(luJama.u))
		println(buildFromJama(luJama.l))
		println(buildFromJama(luJama.l.times(luJama.u)))
		println()

		val lu = matrix.lu()
		println(lu.u)
		println(lu.l)
		println(lu.pivot)
		println(lu.l * lu.u)
	}
}

object Test2 {
	@JvmStatic
	fun main(args: Array<String>) {
		val matrix = random(1000, 1000)
		val jama = matrix.toJamaMatrix()

		generateSequence {
			measureTimeMillis {
				matrix.rowEchelon()
			}.also(::println)
		}
			.drop(5)
			.take(10)
			.average()
			.let(::println)

		println()

		generateSequence {
			measureTimeMillis {
				LUDecomposition(jama)
			}.also(::println)
		}
			.drop(5)
			.take(10)
			.average()
			.let(::println)

		println()

		generateSequence {
			measureTimeMillis {
				matrix.lu()
			}.also(::println)
		}
			.drop(5)
			.take(10)
			.average()
			.let(::println)
	}
}
