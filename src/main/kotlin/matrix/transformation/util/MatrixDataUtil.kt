package matrix.transformation.util

interface MatrixDataUtil {

	fun removeRow(row: Int): MatrixDataUtil

	fun removeColumn(column: Int): MatrixDataUtil

	fun replaceRow(row: Int, elements: List<Double>): MatrixDataUtil

	fun replaceColumn(column: Int, elements: List<Double>): MatrixDataUtil

	fun splitRow(row: Int): List<Double>

	fun splitColumn(column: Int): List<Double>

}