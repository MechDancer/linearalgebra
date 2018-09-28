package org.mechdancer.algebra.equation

interface SuperSolver {
	fun solve(equation: LinearEquation): Solution
}
