package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Vector

fun Vector.toListMatrix() = ListMatrix(1, toList())
fun Vector.toListMatrixRow() = ListMatrix(dimension, toList())

fun listMatrixOf(row: Int, column: Int, block: (Int, Int) -> Double) =
	ListMatrix(column, List(row * column) { block(it / column, it % column) })

fun listMatrixOfZero(row: Int, column: Int) =
	listMatrixOf(row, column) { _, _ -> .0 }

fun listMatrixOfUnit(dim: Int) =
	listMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }
