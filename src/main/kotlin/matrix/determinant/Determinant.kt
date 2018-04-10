package matrix.determinant

import matrix.DefineType
import matrix.MatrixData

interface Determinant {

	val value: Double

	val data: MatrixData

	val column: Int

	val row: Int

	val dimension: Int

	val defineType: DefineType

	fun getCofactor(row: Int, column: Int): Determinant

	fun getAlgebraCofactor(row: Int, column: Int): Double

}
