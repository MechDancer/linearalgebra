package org.mechdancer.algebra

import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.implement.equation.builder.equations
import kotlin.math.sqrt

fun main(args: Array<String>) {
	val a = equations {
		this[-2, -2] = -2
		this[sqrt(3.0) - 2, -2] = (sqrt(3.0) - 4) / 2
		this[-2, 0] = -1 - 1.5 * 0.21
	}
	val b = equations {
		this[-2, -2] = -2
		this[sqrt(3.0) - 2, -2] = (sqrt(3.0) - 4) / 2
		this[-2, 0] = -1 - 1.5 * 0.21
	}
	listOf(a, b).flatten().toSet()

	equations {
		this[-2, -2] = -2
		this[sqrt(3.0) - 2, -2] = (sqrt(3.0) - 4) / 2
		this[-2, 0] = -1 - 1.5 * 0.21
	}
		.also { println(it.matrixView()) }
		.solve()
		.let(::println)
}
