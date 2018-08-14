package org.mechdancer.algebra.matrix.determinant

import org.mechdancer.algebra.matrix.MatrixData

interface Determinant {

	val data: MatrixData

	val column: Int

	val row: Int

	val dimension: Int

	fun calculate(): Double

	fun getCofactor(row: Int, column: Int): Determinant

	fun getAlgebraCofactor(row: Int, column: Int): Double

}
