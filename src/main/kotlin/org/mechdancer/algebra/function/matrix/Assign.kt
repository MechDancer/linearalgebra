package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.ValueMutableMatrix
import org.mechdancer.algebra.implement.matrix.ArrayMatrix

// 对矩阵各项原地操作
private inline fun ValueMutableMatrix.forEachAssign(block: (Double) -> Double) =
	(this as? ArrayMatrix)
		?.data
		?.let { it.forEachIndexed { i, value -> it[i] = block(value) } }
		?: run {
			for (r in 0 until row)
				for (c in 0 until column)
					this[r, c] = block(this[r, c])
		}

/**
 * 原地数乘
 */
operator fun ValueMutableMatrix.timesAssign(k: Number) {
	val arg = k.toDouble()
	forEachAssign { it * arg }
}

/**
 * 原地除以数
 */
operator fun ValueMutableMatrix.divAssign(k: Number) {
	val arg = k.toDouble()
	forEachAssign { it / arg }
}

//对两个矩阵对应项操作
private fun ValueMutableMatrix.zipAssign(
	other: Matrix,
	block: (Double, Double) -> Double
) {
	assertSameSize(this, other)
	for (r in 0 until row)
		for (c in 0 until column)
			this[r, c] = block(this[r, c], other[r, c])
}

/**
 * 原地加上另一个矩阵
 */
operator fun ValueMutableMatrix.plusAssign(other: Matrix) =
	zipAssign(other) { a, b -> a + b }

/**
 * 原地减去另一个矩阵
 */
operator fun ValueMutableMatrix.minusAssign(other: Matrix) =
	zipAssign(other) { a, b -> a - b }

/**
 * 原地转置
 * 只适用于方阵
 */
fun ValueMutableMatrix.transposeAssign() {
	assertSquare()
	for (r in 0 until row)
		for (c in 0 until r) {
			val temp = get(r, c)
			set(r, c, get(c, r))
			set(c, r, temp)
		}
}

/**
 * 原地通过行初等变换变为行阶梯型阵
 */
fun ValueMutableMatrix.rowEchelonAssignWith(
	other: Iterable<ValueMutableMatrix>
) = apply {
	//固定行数
	var fixed = 0
	//按列化简
	for (c in 0 until column) {
		//在此列所有未固定的行中
		(fixed until row)
			//找到第一个非零元素
			.firstOrNull { r -> get(r, c) != .0 }
			//将其所在行交换到未固定的首行
			?.let { r ->
				exchangeRow(r, fixed)
				other.forEach { it.exchangeRow(r, fixed) }
			}
		//全为零则直接查找下一列
			?: continue

		//取出这一行的首元
		val head = get(fixed, c)
		//用首元将此列未固定的其他元素化为零
		for (r in fixed + 1 until row) {
			val k = -get(r, c) / head
			plusToRow(k, fixed, r)
			other.forEach { it.plusToRow(k, fixed, r) }
			//强保证化为零有效
			set(r, c, .0)
		}

		//固定行数加一
		++fixed
	}
}

/**
 * 原地通过行初等变换变为最简行阶梯型阵
 */
fun ValueMutableMatrix.simplifyAssignWith(
	other: Iterable<ValueMutableMatrix>
) = rowEchelonAssignWith(other)
	.apply {
		var c = column
		//清零上三角区
		for (r in (0 until row).reversed()) {
			//找到本行最后一个非零元素
			var tail: Double
			do {
				tail = get(r, --c)
			} while (c >= 0 && tail == .0)

			//有全零行，不再继续化简
			if (c < 0) break

			//用首元将此列未固定的其他元素化为零
			for (t in (0 until r)) {
				val k = -get(t, c) / tail
				plusToRow(k, r, t)
				other.forEach { it.plusToRow(k, r, t) }
				//强保证化为零有效
				set(t, c, .0)
			}
		}

		c = 0
		//主对角线归一化
		for (r in 0 until row)
			while (c < column) {
				val temp = get(r, c++)
				if (temp != .0) {
					timesRow(r, 1 / temp)
					other.forEach { it.timesRow(r, 1 / temp) }
					break
				}
			}
	}

/**
 * 原地通过行初等变换变为行阶梯型阵
 */
fun ValueMutableMatrix.rowEchelonAssignWith(
	vararg other: ValueMutableMatrix
) = rowEchelonAssignWith(other.toList())

/**
 * 原地通过行初等变换变为最简行阶梯型阵
 */
fun ValueMutableMatrix.simplifyAssignWith(
	vararg other: ValueMutableMatrix
) = simplifyAssignWith(other.toList())

/**
 * 原地通过行初等变换变为行阶梯型阵
 */
fun ValueMutableMatrix.rowEchelonAssign() = rowEchelonAssignWith()

/**
 * 原地通过行初等变换变为最简行阶梯型阵
 */
fun ValueMutableMatrix.simplifyAssign() = simplifyAssignWith()
