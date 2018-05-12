package cn.berberman.algebra.equation

import cn.berberman.algebra.vector.Vector

@FunctionalInterface
interface Solver {
	fun solve(equation: LinearEquation): Vector
}