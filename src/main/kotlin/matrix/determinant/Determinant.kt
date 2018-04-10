package matrix.determinant

interface Determinant {

	val value: Double

	fun getCofactor(row: Int, column: Int): Determinant

	fun getAlgebraCofactor(row: Int, column: Int): Double

}
