package matrix.transformation.impl

import matrix.Matrix
import matrix.MatrixData
import matrix.toMatrix
import matrix.transformation.ElementaryTransformation
import matrix.transformation.util.impl.MutableMatrixDataUtil

class ElementaryTransformationImpl(data: MatrixData) : ElementaryTransformation {

	private val util = MutableMatrixDataUtil(data)

	override fun getResult(): Matrix = util.getData().toMatrix()

	override fun rowMultiply(row: Int, k: Double) {
		val temp = util.splitRow(row).map { it * k }
		util.replaceRow(row, temp)
	}

	override fun rowSwap(row1: Int, row2: Int) {
		val temp = util.splitRow(row1)
		util.replaceRow(row1, util.splitRow(row2))
		util.replaceRow(row2, temp)
	}

	override fun rowAddTo(from: Int, to: Int, k: Double) {
		val temp = util.splitRow(from).map { it * k }
		util.replaceRow(to, util.splitRow(to).mapIndexed { index, d -> d + temp[index] })
	}

	override fun columnMultiply(column: Int, k: Double) {
		val temp = util.splitColumn(column).map { it * k }
		util.replaceColumn(column, temp)
	}

	override fun columnSwap(column1: Int, column2: Int) {
		val temp = util.splitRow(column1)
		util.replaceRow(column1, util.splitRow(column2))
		util.replaceRow(column2, temp)
	}

	override fun columnAddTo(from: Int, to: Int, k: Double) {
		val temp = util.splitColumn(from).map { it * k }
		util.replaceRow(to, util.splitColumn(to).mapIndexed { index, d -> d + temp[index] })
	}
}