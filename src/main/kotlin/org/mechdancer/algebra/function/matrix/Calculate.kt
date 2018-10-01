package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.dot
import org.mechdancer.algebra.implement.matrix.*
import org.mechdancer.algebra.implement.vector.toListVector

//矩阵转换为List<Double>
private fun Matrix.toList() =
	(this as? ListMatrix)?.list
		?: (this as? ArrayMatrix)?.array?.toList()
		?: rows.flatMap { it.toList() }

operator fun Number.times(m: Matrix): Matrix {
	val temp = toDouble()
	return ListMatrix(m.column, m.toList().map { it * temp })
}

operator fun Matrix.times(k: Number) = k * this
operator fun Matrix.div(k: Number) = (1 / k.toDouble()) * this

//逐项应用某种操作
private fun Matrix.zip(other: Matrix, block: (Double, Double) -> Double): Matrix {
	assertSameSize(this, other)
	return ListMatrix(column, toList().zip(other.toList(), block))
}

operator fun Matrix.plus(other: Matrix) = zip(other) { a, b -> a + b }
operator fun Matrix.minus(other: Matrix) = zip(other) { a, b -> a - b }

operator fun Matrix.times(vector: Vector): Vector {
	assertCanMultiply(this, vector)
	return rows.map { it dot vector }.toListVector()
}

operator fun Matrix.times(other: Matrix): Matrix {
	assertCanMultiply(this, other)
	val period = 0 until column
	return listMatrixOf(row, other.column) { r, c ->
		period.sumByDouble { i -> this[r, i] * other[i, c] }
	}
}

operator fun Matrix.div(other: Matrix) = other.inverse()?.let { times(it) }

operator fun Matrix.invoke(right: Matrix) = times(right)
operator fun Matrix.invoke(right: Vector) = times(right)
operator fun Matrix.invoke(right: Number) = times(right)

operator fun Matrix.unaryPlus() = this
operator fun Matrix.unaryMinus() = ListMatrix(column, toList().map { -it })
fun Matrix.transpose() = listMatrixOf(column, row) { r, c -> this[c, r] }
fun Matrix.rowEchelon() = toArrayMatrix().rowEchelonAssign()

fun Matrix.cofactorOf(r: Int, c: Int) = Cofactor(this, r, c)

private fun Matrix.algebraCofactorOf(r: Int, c: Int): Double =
	(if ((r + c) % 2 == 0) 1 else -1) * cofactorOf(r, c).determinantValue()

fun Matrix.companion(): Matrix {
	assertSquare()
	return listMatrixOf(row, column) { r, c -> algebraCofactorOf(c, r) }
}

fun Matrix.determinantValue(): Double {
	assertSquare()
	return when (row) {
		1    -> get(0, 0)
		2    -> get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0)
		rank -> (0 until column).sumByDouble { c ->
			get(0, c) * algebraCofactorOf(0, c)
		}
		else -> .0
	}
}

fun Matrix.inverse() = toArrayMatrix().inverseDestructive()
