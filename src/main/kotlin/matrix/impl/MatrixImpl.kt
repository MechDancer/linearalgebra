package matrix.impl

import matrix.Matrix
import matrix.MatrixData
import matrix.determinant.Determinant
import matrix.determinant.impl.DeterminantImpl
import matrix.toMatrix
import matrix.transformation.ElementaryTransformation
import matrix.transformation.impl.ElementaryTransformationImpl
import vector.Vector
import vector.toVector
import kotlin.math.abs

class MatrixImpl(override val data: MatrixData) : Matrix, Cloneable {


	override val row: Int = data.size

	override val column: Int = data.first().size

	override val isSquare: Boolean = row == column

	override val dimension: Int = if (!isSquare) -1 else row

	override val rank: Int by lazy {
		rowEchelon().data.count { it.all { it != .0 } }
	}

	private val transposeLazy: Matrix by lazy {
		List(column) { r ->
			List(row) { c ->
				this[c, r]
			}
		}.toMatrix()
	}

	private val companionLazy: Matrix by lazy {
		if (!isSquare) throw IllegalStateException("伴随矩阵未定义")
		toDeterminant().let {
			List(row) { r ->
				List(column) { c ->
					it.getAlgebraCofactor(r, c)
				}
			}
		}.toMatrix().transpose()
	}

	private val inverseLazy: Matrix by lazy {
		if (!isSquare) throw IllegalStateException("逆矩阵未定义")
		val d = det()
		if (d == .0) throw IllegalStateException("行列式为零不能通过此种方法求逆")
		companion() / d
	}

	private val determinantLazy: Determinant by lazy { DeterminantImpl(this) }

	private val detLazy: Double by lazy {
		if (!isSquare) throw IllegalStateException("行列式未定义")
		toDeterminant().calculate()
	}

	private val rowEchelonLazy: Matrix by lazy { TODO("还没写化简") }

	init {
		if (row == 0 || column == 0) throw IllegalArgumentException("不能构虚无矩阵")
		if (data.any { it.size != column })
			throw IllegalArgumentException("矩阵参数错误")
	}

	override fun get(row: Int, column: Int): Double =
			data[row][column]


	override fun plus(other: Matrix): Matrix {
		checkDimension(other)
		return data.indices.map { r -> data[r].indices.map { c -> this[r, c] + other[r, c] } }
				.toMatrix()
	}

	override fun minus(other: Matrix): Matrix {
		checkDimension(other)
		return data.indices.map { r -> data[r].indices.map { c -> this[r, c] - other[r, c] } }
				.toMatrix()
	}

	override fun times(k: Double): Matrix = data.map { it.map { it * k } }.toMatrix()

	override fun times(other: Matrix): Matrix = List(row) { r ->
		if (column != other.row) throw IllegalArgumentException("该乘法未定义")
		List(other.column) { c ->
			(0 until column).sumByDouble {
				this[r, it] * other[it, c]
			}
		}
	}.toMatrix()

	override fun times(vector: Vector): Vector {
		if (vector.dimension != column) throw IllegalArgumentException("维度错误")
		return (this * vector.toMatrix()).toVector()
	}

	override fun invoke(other: Matrix): Matrix = times(other)

	override fun invoke(vector: Vector): Vector = times(vector)

	override fun div(other: Matrix): Matrix = this * other.inverse()

	override fun div(k: Double): Matrix = data.map { it.map { it / k } }.toMatrix()

	override infix fun pow(n: Int): Matrix {
		if (!isSquare) throw IllegalStateException("乘方未定义")
		if (n <= 0) throw IllegalArgumentException("至少乘一次方")

		return (1 until n).fold(this as Matrix) { acc, _ ->
			acc * this
		}
	}

	override fun toDeterminant(): Determinant = determinantLazy

	override fun det(): Double = detLazy

	override fun elementaryTransformation(block: ElementaryTransformation.() -> Unit) =
			ElementaryTransformationImpl(data).apply(block).getResult()


	override fun companion(): Matrix = companionLazy

	override fun transpose(): Matrix = transposeLazy

	override fun inverse(): Matrix = inverseLazy

	override fun rowEchelon(): Matrix = rowEchelonLazy

	private fun checkDimension(other: Matrix) = if (this.dimension != other.dimension)
		throw IllegalArgumentException("维度错误") else Unit

	override fun toString(): String = buildString {
		val maxDataLength = data.flatten().map { it.toString().length }.max()!!
		append(" ".repeat(maxDataLength * column / 2))
		appendln("Matrix ($row x $column)")
		data.forEachIndexed { r, e ->
			when (r) {
				0       -> append("┌")
				row - 1 -> append("└")
				else    -> append("│")
			}
			e.indices.forEach { c ->
				val data = this@MatrixImpl[r, c]
				var dL = abs(maxDataLength - data.toString().length)
				dL /= 2
				val left: Int = if (dL % 2 == 0) dL else dL + 1
				append(" ".repeat(left))
				append(" $data ")
				append(" ".repeat(dL))
				if (c == column - 1)
					when (r) {
						0       -> append("┐")
						row - 1 -> append("┘")
						else    -> append("│")
					}
			}
			appendln()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (other !is Matrix) return false
		return other.data == data
	}

	override fun hashCode(): Int = data.hashCode() + javaClass.hashCode()

}