package matrix.transformation.util.impl

//
//import matrix.MatrixData
//import matrix.transformation.util.MatrixDataUtil
//
//@Deprecated("还是用可变的比较稳妥", ReplaceWith(" MutableMatrixDataUtil"))
//object ImmutableMatrixDataUtil {
//
//	private val column = data.first().size
//
//	private val row = data.size
//
//	override fun removeRow(row: Int): MatrixData {
//		checkRowRange(row)
//		return List(this.row - 1) { r ->
//			List(column) { c ->
//				if (r < row)
//					data[r][c]
//				else data[r + 1][c]
//			}
//		}
//	}
//
//	override fun removeColumn(column: Int): MatrixData {
//		checkColumnRange(column)
//		return List(row) { r ->
//			List(this.column - 1) { c ->
//				if (c < column)
//					data[r][c]
//				else data[r][c + 1]
//			}
//		}
//	}
//
//	override fun replaceRow(row: Int, elements: List<Double>): MatrixData {
//		if (elements.size != column) throw IllegalArgumentException("元素项数不同")
//		checkRowRange(row)
//		return List(this.row) { r ->
//			List(column) { c ->
//				if (r == row) elements[c]
//				else data[r][c]
//			}
//		}
//	}
//
//	override fun replaceColumn(column: Int, elements: List<Double>): MatrixData {
//		if (elements.size != row) throw IllegalArgumentException("元素项数不同")
//		checkColumnRange(column)
//		return List(row) { r ->
//			List(this.column) { c ->
//				if (c == column) elements[r]
//				else data[r][c]
//			}
//		}
//	}
//
//	override fun splitRow(row: Int): List<Double> {
//		checkRowRange(row)
//		return List(column) { c ->
//			data[row][c]
//		}
//	}
//
//	override fun splitColumn(column: Int): List<Double> {
//		checkColumnRange(column)
//		return List(row) { r ->
//			data[r][column]
//		}
//	}
//
//
//	private fun checkRowRange(row: Int) = if (row !in 0 until this.row)
//		throw IllegalArgumentException("行数错误!") else Unit
//
//
//	private fun checkColumnRange(column: Int) = if (column !in 0 until this.column)
//		throw IllegalArgumentException("列数错误!") else Unit
//
//}