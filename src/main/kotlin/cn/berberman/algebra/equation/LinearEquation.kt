package cn.berberman.algebra.equation

import cn.berberman.algebra.matrix.Matrix
import cn.berberman.algebra.vector.Vector

interface LinearEquation {

	val coefficient: Matrix

	val constant: Vector

	val isHomogeneous: Boolean

	fun solve(solver: Solver): Vector
}