package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.transpose
import org.mechdancer.algebra.implement.matrix.ArrayMatrix
import org.mechdancer.algebra.implement.matrix.ListMatrix
import org.mechdancer.algebra.implement.matrix.builder.BuilderMode.Immutable

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

infix fun Iterable<Number>.foldToRowOf(length: Int) = ListMatrix(length, map { it.toDouble() })

infix fun Iterable<Number>.foldToRows(count: Int) = map { it.toDouble() }.let { ListMatrix(it.size / count, it) }

infix fun Iterable<Number>.foldToColumnOf(length: Int) = foldToRowOf(length).transpose()

infix fun Iterable<Number>.foldToColumns(count: Int) = foldToRows(count).transpose()

// DoubleArray -> Matrix

infix fun DoubleArray.foldToRowOf(length: Int) = ArrayMatrix(length, this)

infix fun DoubleArray.foldToRows(count: Int) = foldToRowOf(size / count)

infix fun DoubleArray.foldToColumnOf(length: Int) = foldToRowOf(length).transpose()

infix fun DoubleArray.foldToColumns(count: Int) = foldToRows(count).transpose()

// Diagonal Matrix

fun Iterable<Number>.toDiagonalListMatrix() = toList().let {
	listMatrixOf(it.size, it.size) { r, c -> if (r == c) it[r] else .0 }
}

fun Iterable<Number>.toDiagonalArrayMatrix() = toList().let {
	arrayMatrixOf(it.size, it.size) { r, c -> if (r == c) it[r] else .0 }
}

fun Array<Number>.toDiagonalListMatrix() =
	listMatrixOf(size, size) { r, c -> if (r == c) get(r) else .0 }

fun Array<Number>.toDiagonalArrayMatrix() =
	arrayMatrixOf(size, size) { r, c -> if (r == c) get(r) else .0 }

fun DoubleArray.toDiagonalListMatrix() =
	listMatrixOf(size, size) { r, c -> if (r == c) get(r) else .0 }

fun DoubleArray.toDiagonalArrayMatrix() =
	arrayMatrixOf(size, size) { r, c -> if (r == c) get(r) else .0 }

// Zero Matrix

fun arrayMatrixOfZero(row: Int, column: Int): ArrayMatrix =
	arrayMatrixOf(row, column) { _, _ -> .0 }

fun arrayMatrixOfZero(dim: Int): ArrayMatrix =
	arrayMatrixOf(dim, dim) { _, _ -> .0 }

// Unit Matrix

fun arrayMatrixOfUnit(dim: Int): ArrayMatrix =
	arrayMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

// Number Matrix

infix fun Number.toArrayMatrix(dim: Int): ArrayMatrix {
	val value = toDouble()
	return arrayMatrixOf(dim, dim) { r, c -> if (r == c) value else .0 }
}

// Matrix -> Matrix

fun Matrix.toListMatrix(): ListMatrix =
	this as? ListMatrix
		?: (this as? ArrayMatrix)?.let { ListMatrix(column, it.data.toList()) }
		?: listMatrixOf(row, column, ::get)

fun Matrix.toArrayMatrix(): ArrayMatrix =
	(this as? ArrayMatrix)?.clone()
		?: (this as? ListMatrix)?.let { ArrayMatrix(column, it.data.toDoubleArray()) }
		?: arrayMatrixOf(row, column, ::get)

// DSL Builder

fun matrix(
	mode: BuilderMode = Immutable,
	block: MatrixBuilder.() -> Unit
) = MatrixBuilder().apply(block).build(mode)

fun blockMatrix(
	mode: BuilderMode = Immutable,
	block: BlockMatrixBuilder.() -> Unit
) = BlockMatrixBuilder().apply(block).build(mode)
