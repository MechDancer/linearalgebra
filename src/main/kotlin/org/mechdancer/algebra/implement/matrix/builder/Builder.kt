package org.mechdancer.algebra.implement.matrix.builder

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.matrix.determinantValue
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

// Array -> Matrix

infix fun Array<Number>.foldToRowOf(length: Int) = ArrayMatrix(length, map { it.toDouble() }.toDoubleArray())

infix fun Array<Number>.foldToRows(count: Int) = foldToRowOf(size / count)

infix fun Array<Number>.foldToColumnOf(length: Int) = foldToRowOf(length).transpose()

infix fun Array<Number>.foldToColumns(count: Int) = foldToRows(count).transpose()

// Zero Matrix

fun listMatrixOfZero(row: Int, column: Int): ListMatrix =
	listMatrixOf(row, column) { _, _ -> .0 }

fun arrayMatrixOfZero(row: Int, column: Int): ArrayMatrix =
	arrayMatrixOf(row, column) { _, _ -> .0 }

fun listMatrixOfZero(dim: Int): ListMatrix =
	listMatrixOf(dim, dim) { _, _ -> .0 }

fun arrayMatrixOfZero(dim: Int): ArrayMatrix =
	arrayMatrixOf(dim, dim) { _, _ -> .0 }

// AngleUnit Matrix

fun listMatrixOfUnit(dim: Int): ListMatrix =
	listMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

fun arrayMatrixOfUnit(dim: Int): ArrayMatrix =
	arrayMatrixOf(dim, dim) { r, c -> if (r == c) 1.0 else .0 }

// Number Matrix

infix fun Number.toListMatrix(dim: Int): ListMatrix {
	val value = toDouble()
	return listMatrixOf(dim, dim) { r, c -> if (r == c) value else .0 }
}

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

// super shortcut

object O {
	@JvmStatic
	operator fun invoke(m: Int) = listMatrixOfZero(m, m)

	@JvmStatic
	operator fun invoke(m: Int, n: Int) = listMatrixOfZero(m, n)
}

object I {
	@JvmStatic
	operator fun invoke(n: Int) = listMatrixOfUnit(n)
}

object N {
	@JvmStatic
	operator fun invoke(n: Int, x: Number) = x toListMatrix n

	@JvmStatic
	operator fun invoke(x: Number) = x toListMatrix 1
}

object Det {
	@JvmStatic
	operator fun invoke(matrix: Matrix) = matrix.determinantValue()
}
