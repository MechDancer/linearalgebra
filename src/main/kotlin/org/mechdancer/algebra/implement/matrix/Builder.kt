package org.mechdancer.algebra.implement.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector

// Vector -> Matrix

fun Vector.toListMatrix() = ListMatrix(1, toList())
fun Vector.toListMatrixRow() = ListMatrix(dim, toList())

fun Vector.toArrayMatrix() = ArrayMatrix(1, toList().toDoubleArray())
fun Vector.toArrayMatrixRow() = ArrayMatrix(dim, toList().toDoubleArray())

// Function -> Matrix

fun listMatrixOf(row: Int, column: Int, block: (Int, Int) -> Number): ListMatrix =
	ListMatrix(column, List(row * column) { block(it / column, it % column).toDouble() })

fun arrayMatrixOf(row: Int, column: Int, block: (Int, Int) -> Number): ArrayMatrix =
	ArrayMatrix(column, DoubleArray(row * column) { block(it / column, it % column).toDouble() })

// Zero Matrix

fun listMatrixOfZero(row: Int, column: Int): ListMatrix =
	listMatrixOf(row, column) { _, _ -> .0 }

fun arrayMatrixOfZero(row: Int, column: Int): ArrayMatrix =
	arrayMatrixOf(row, column) { _, _ -> .0 }

// Unit Matrix

fun listMatrixOfUnit(dim: Int): ListMatrix =
	listMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

fun arrayMatrixOfUnit(dim: Int): ArrayMatrix =
	arrayMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

// Matrix -> Matrix

fun Matrix.toListMatrix(): ListMatrix =
	(this as? ListMatrix)
		?: (this as? ArrayMatrix)?.let { ListMatrix(column, it.array.toList()) }
		?: listMatrixOf(row, column, ::get)

fun Matrix.toArrayMatrix(): ArrayMatrix =
	(this as? ArrayMatrix)?.clone()
		?: (this as? ListMatrix)?.let { ArrayMatrix(column, it.list.toDoubleArray()) }
		?: arrayMatrixOf(row, column, ::get)
