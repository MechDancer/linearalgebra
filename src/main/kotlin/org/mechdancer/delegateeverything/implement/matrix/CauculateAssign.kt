package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
import org.mechdancer.delegateeverything.core.MutableMatrix
import org.mechdancer.delegateeverything.core.ValueMutableMatrix

//对矩阵各项操作
private fun ValueMutableMatrix.forEach(block: (Double) -> Double) =
	(this as? ArrayMatrix)?.array
		?.let { it.forEachIndexed { i, value -> it[i] = block(value) } }
		?: run {
			for (r in 0 until row)
				for (c in 0 until column)
					this[r, c] = block(this[r, c])
		}

operator fun ValueMutableMatrix.timesAssign(k: Number) = forEach { it * k.toDouble() }
operator fun ValueMutableMatrix.divAssign(k: Number) = forEach { it / k.toDouble() }

private fun differentSizeException(a: Matrix, b: Matrix) =
	IllegalArgumentException("operate two matrix of different size (${a.row}*${a.column} and ${b.row}*${b.column})")

//对两个矩阵对应项操作
private fun ValueMutableMatrix.zip(other: Matrix, block: (Double, Double) -> Double) =
	takeIf { row == other.row && column == other.column }
		?.run {
			for (r in 0 until row)
				for (c in 0 until column)
					this[r, c] = block(this[r, c], other[r, c])
		}
		?: throw differentSizeException(this, other)

operator fun ValueMutableMatrix.plusAssign(other: Matrix) = zip(other) { a, b -> a + b }
operator fun ValueMutableMatrix.minusAssign(other: Matrix) = zip(other) { a, b -> a - b }

/**
 * 通过行初等变换变为行阶梯型阵
 */
fun ValueMutableMatrix.rowEchelon() =
	apply {
		//固定行数
		var fixed = 0
		//按列化简
		for (c in 0 until column) {
			//在此列所有未固定的行中
			(fixed until row)
				//找到第一个非零元素
				.firstOrNull { r -> get(r, c) != .0 }
				//将其所在行交换到未固定的首行
				?.let { exchangeRow(it, fixed) }
			//全为零则直接查找下一列
				?: continue

			//取出这一行的首元
			val head = get(fixed, c)
			//用首元将此列未固定的其他元素化为零
			for (r in fixed + 1 until row) {
				plusToRow(-get(r, c) / head, fixed, r)
				set(r, c, .0) //强保证化为零有效
			}

			//固定行数加一
			++fixed
		}
	}

/**
 * 获取余子式
 */
fun MutableMatrix.getCofactor(r: Int, c: Int) =
	apply {
		removeRow(r)
		removeColumn(c)
	}
