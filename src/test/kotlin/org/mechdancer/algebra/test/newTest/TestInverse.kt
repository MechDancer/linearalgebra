package org.mechdancer.algebra.test.newTest

import org.mechdancer.delegateeverything.implement.matrix.*

fun main(args: Array<String>) {
	val o = ListMatrix(3, listOf(
		1.0, 2.0, 3.0,
		4.0, 2.0, 2.0,
		5.0, 1.0, 7.0
	))
	val m = o.toArrayMatrix()
	val u = arrayMatrixOfUnit(3)
	println("m = ")
	println(m)
	println()
	println("u = ")
	println(u)
	println()

	m.simplifyAssignWith(u)
	println("m = ")
	println(m)
	println()
	println("u = ")
	println(u)
	println()

	println("m * u = ")
	println(o * u)

	println(o.inverse())
}
