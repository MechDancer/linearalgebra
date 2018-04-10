package matrix

interface ElementaryTransformation {

	fun rowMultiply(row: Int, k: Double)

	fun rowSwap(row1: Int, row2: Int)

	fun rowAddTo(row1: Int, row2: Int, k: Double)

	fun columnMultiply(column: Int, k: Double)

	fun columnSwap(column1: Int, column2: Int)

	fun columnAddTo(column1: Int, column2: Int, k: Double)

}