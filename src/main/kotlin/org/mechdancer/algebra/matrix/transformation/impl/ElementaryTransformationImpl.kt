package org.mechdancer.algebra.matrix.transformation.impl

import org.mechdancer.algebra.matrix.Matrix
import org.mechdancer.algebra.matrix.MatrixData
import org.mechdancer.algebra.matrix.toMatrix
import org.mechdancer.algebra.matrix.transformation.ElementaryTransformation
import org.mechdancer.algebra.matrix.transformation.util.impl.MutableMatrixDataUtil

class ElementaryTransformationImpl(data: MatrixData) : ElementaryTransformation {

	override val dataUtil = MutableMatrixDataUtil(data)

	override fun getResult(): Matrix = dataUtil.getData().toMatrix()

	override fun rowMultiply(row: Int, k: Double) {
		val temp = dataUtil.splitRow(row).map { it * k }
		dataUtil.replaceRow(row, temp)
	}

	override fun rowSwap(row1: Int, row2: Int) {
		val temp = dataUtil.splitRow(row1)
		dataUtil.replaceRow(row1, dataUtil.splitRow(row2))
		dataUtil.replaceRow(row2, temp)
	}

	override fun rowAddTo(from: Int, to: Int, k: Double) {
		val temp = dataUtil.splitRow(from).map { it * k }
		dataUtil.replaceRow(to, dataUtil.splitRow(to).mapIndexed { index, d -> d + temp[index] })
	}

	override fun columnMultiply(column: Int, k: Double) {
		val temp = dataUtil.splitColumn(column).map { it * k }
		dataUtil.replaceColumn(column, temp)
	}

	override fun columnSwap(column1: Int, column2: Int) {
		val temp = dataUtil.splitRow(column1)
		dataUtil.replaceRow(column1, dataUtil.splitRow(column2))
		dataUtil.replaceRow(column2, temp)
	}

	override fun columnAddTo(from: Int, to: Int, k: Double) {
		val temp = dataUtil.splitColumn(from).map { it * k }
		dataUtil.replaceRow(to, dataUtil.splitColumn(to).mapIndexed { index, d -> d + temp[index] })
	}
}