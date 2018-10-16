package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.transpose
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.MatrixBuilder.Mode
import org.mechdancer.algebra.implement.matrix.builder.MatrixBuilder.Mode.Constant

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

// List -> Matrix

infix fun List<Number>.foldAsRowOf(length: Int) = ListMatrix(length, this.map { it.toDouble() })

infix fun List<Number>.foldToRows(count: Int) = foldAsRowOf(size / count)

infix fun List<Number>.foldAsColumnOf(length: Int) = foldAsRowOf(length).transpose()

infix fun List<Number>.foldToColumns(count: Int) = foldToRows(count).transpose()

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
	this as? ListMatrix
		?: (this as? ArrayMatrix)?.let { ListMatrix(column, it.array.toList()) }
		?: listMatrixOf(row, column, ::get)

fun Matrix.toArrayMatrix(): ArrayMatrix =
	(this as? ArrayMatrix)?.clone()
		?: (this as? ListMatrix)?.let { ArrayMatrix(column, it.list.toDoubleArray()) }
		?: arrayMatrixOf(row, column, ::get)

// DSL Builder

fun matrix(
	mode: Mode = Constant,
	block: MatrixBuilder.() -> Unit
) = MatrixBuilder().apply(block).build(mode)
