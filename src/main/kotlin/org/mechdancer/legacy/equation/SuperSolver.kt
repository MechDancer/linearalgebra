package org.mechdancer.legacy.equation

interface SuperSolver {
	fun solve(equation: LinearEquation): Solution
}
