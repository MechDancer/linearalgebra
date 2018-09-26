package org.mechdancer.algebra.matrix.transformation.util.impl

import org.mechdancer.algebra.columnNumberError
import org.mechdancer.algebra.matrix.MatrixData
import org.mechdancer.algebra.matrix.MatrixElement
import org.mechdancer.algebra.matrix.transformation.util.MatrixDataUtil
import org.mechdancer.algebra.rowNumberError

class MutableMatrixDataUtil(data: MatrixData) : MatrixDataUtil {

	private val mutableData =
			data.map { it.toMutableList() }.toMutableList()


	fun addRow(data: MatrixElement) = apply {
		mutableData.add(data.toMutableList())
	}

	fun addColumn(data: MatrixElement) = apply {
		mutableData.forEachIndexed { c, row ->
			row.add(data[c])
		}
	}

	override fun removeRow(row: Int): MatrixDataUtil = apply {
		mutableData.removeAt(row)
	}

	override fun removeColumn(column: Int): MatrixDataUtil = apply {
		mutableData.forEach { row ->
			row.removeAt(column)
		}
	}

	override fun replaceRow(row: Int, elements: MatrixElement): MatrixDataUtil = apply {
		if (elements.size != mutableData.first().size) throw IllegalArgumentException("元素项数不同")
		checkRowRange(row)
		mutableData[row] = elements.toMutableList()
	}

	override fun replaceColumn(column: Int, elements: MatrixElement): MatrixDataUtil = apply {
		if (elements.size != mutableData.size) throw IllegalArgumentException("元素项数不同")
		checkColumnRange(column)
		elements.forEachIndexed { i, e ->
			mutableData[i][column] = e
		}
	}

	override fun splitRow(row: Int): MatrixElement {
		checkRowRange(row)
		return mutableData[row]
	}

	override fun splitColumn(column: Int): MatrixElement {
		checkColumnRange(column)
		return List(mutableData.size) {
			mutableData[it][column]
		}
	}

	fun getData(): MatrixData = mutableData.toList()

	private fun checkRowRange(row: Int) = if (row !in 0 until mutableData.size)
		rowNumberError() else Unit


	private fun checkColumnRange(column: Int) = if (column !in 0 until mutableData.first().size)
		columnNumberError() else Unit


}

fun operateMatrixDataMutable(data: MatrixData, block: MutableMatrixDataUtil.() -> Unit) =
		MutableMatrixDataUtil(data).apply(block).getData()