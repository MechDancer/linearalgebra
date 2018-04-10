package matrix.util.impl

import matrix.MatrixData
import matrix.util.MatrixDataUtil

class MutableMatrixDataUtil(data: MatrixData) : MatrixDataUtil {

	private val mutableData =
			data.map { it.toMutableList() }.toMutableList()


	override fun removeRow(row: Int): MatrixDataUtil = apply {
		mutableData.removeAt(row)
	}

	override fun removeColumn(column: Int): MatrixDataUtil = apply {
		mutableData.forEach { row ->
			row.removeAt(column)
		}
	}

	override fun replaceRow(row: Int, elements: List<Double>): MatrixDataUtil = apply {
		if (elements.size != mutableData.first().size) throw IllegalArgumentException("元素项数不同")
		checkRowRange(row)
		mutableData[row] = elements.toMutableList()
	}

	override fun replaceColumn(column: Int, elements: List<Double>): MatrixDataUtil = apply {
		if (elements.size != mutableData.size) throw IllegalArgumentException("元素项数不同")
		checkColumnRange(column)
		elements.forEachIndexed { i, e ->
			mutableData[i][column] = e
		}
	}

	override fun splitRow(row: Int): List<Double> {
		checkRowRange(row)
		return mutableData[row]
	}

	override fun splitColumn(column: Int): List<Double> {
		checkColumnRange(column)
		return List(mutableData.size) {
			mutableData[it][column]
		}
	}

	fun getData(): MatrixData = mutableData.toList()

	private fun checkRowRange(row: Int) = if (row !in 0 until mutableData.size)
		throw IllegalArgumentException("行数错误!") else Unit


	private fun checkColumnRange(column: Int) = if (column !in 0 until mutableData.first().size)
		throw IllegalArgumentException("列数错误!") else Unit


}