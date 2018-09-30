package org.mechdancer.delegateeverything.implement.matrix

import org.mechdancer.delegateeverything.core.Matrix
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

fun main(args: Array<String>) {
	val m = ArrayMatrix(3, doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))
	println("A =")
	println(m)
	m += listMatrixOfUnit(3)
	println("B =")
	println(m)
}
