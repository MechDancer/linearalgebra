package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.Vector

// Vector -> Matrix

fun Vector.toListMatrix() = ListMatrix(1, toList())
fun Vector.toListMatrixRow() = ListMatrix(dim, toList())

fun Vector.toArrayMatrix() = ArrayMatrix(1, toList().toDoubleArray())
fun Vector.toArrayMatrixRow() = ArrayMatrix(dim, toList().toDoubleArray())

fun Vector.toMutableListMatrix() = MutableListMatrix(1, toList().toMutableList())
fun Vector.toMutableListMatrixRow() = MutableListMatrix(dim, toList().toMutableList())

// Function -> Matrix

fun listMatrixOf(row: Int, column: Int, block: (Int, Int) -> Number): ListMatrix =
	ListMatrix(column, List(row * column) { block(it / column, it % column).toDouble() })

fun arrayMatrixOf(row: Int, column: Int, block: (Int, Int) -> Number): ArrayMatrix =
	ArrayMatrix(column, DoubleArray(row * column) { block(it / column, it % column).toDouble() })

fun mutableListMatrixOf(row: Int, column: Int, block: (Int, Int) -> Number): MutableListMatrix =
	MutableListMatrix(column, MutableList(row * column) { block(it / column, it % column).toDouble() })

// Zero Matrix

fun listMatrixOfZero(row: Int, column: Int): ListMatrix =
	listMatrixOf(row, column) { _, _ -> .0 }

fun arrayMatrixOfZero(row: Int, column: Int): ArrayMatrix =
	arrayMatrixOf(row, column) { _, _ -> .0 }

fun mutableListMatrixOfZero(row: Int, column: Int): MutableListMatrix =
	mutableListMatrixOf(row, column) { _, _ -> .0 }

// Unit Matrix

fun listMatrixOfUnit(dim: Int): ListMatrix =
	listMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

fun arrayMatrixOfUnit(dim: Int): ArrayMatrix =
	arrayMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

fun mutableListMatrixOfUnit(dim: Int): MutableListMatrix =
	mutableListMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

// Matrix -> Matrix

fun Matrix.toListMatrix(): ListMatrix =
	(this as? ListMatrix)
		?: (this as? ArrayMatrix)?.let { ListMatrix(column, it.array.toList()) }
		?: (this as? MutableListMatrix)?.let { ListMatrix(column, it.list.toList()) }
		?: listMatrixOf(row, column, ::get)

fun Matrix.toArrayMatrix(): ArrayMatrix =
	(this as? ArrayMatrix)?.clone()
		?: (this as? ListMatrix)?.let { ArrayMatrix(column, it.list.toDoubleArray()) }
		?: (this as? MutableListMatrix)?.let { ArrayMatrix(column, it.list.toDoubleArray()) }
		?: arrayMatrixOf(row, column, ::get)

fun Matrix.tomutableListMatrix(): MutableListMatrix =
	(this as? MutableListMatrix)?.clone()
		?: (this as? ListMatrix)?.let { MutableListMatrix(column, it.list.toMutableList()) }
		?: (this as? ArrayMatrix)?.let { MutableListMatrix(column, it.array.toMutableList()) }
		?: mutableListMatrixOf(row, column, ::get)
