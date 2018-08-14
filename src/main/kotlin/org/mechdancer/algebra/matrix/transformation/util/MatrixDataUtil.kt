package org.mechdancer.algebra.matrix.transformation.util

import org.mechdancer.algebra.matrix.MatrixElement

interface MatrixDataUtil {

	fun removeRow(row: Int): MatrixDataUtil

	fun removeColumn(column: Int): MatrixDataUtil

	fun replaceRow(row: Int, elements: MatrixElement): MatrixDataUtil

	fun replaceColumn(column: Int, elements: MatrixElement): MatrixDataUtil

	fun splitRow(row: Int): MatrixElement

	fun splitColumn(column: Int): MatrixElement

}