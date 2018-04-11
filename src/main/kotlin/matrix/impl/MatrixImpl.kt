package matrix.impl

import matrix.*
import matrix.transformation.ElementaryTransformation
import matrix.transformation.impl.ElementaryTransformationImpl
import vector.Vector
import vector.toVector
import kotlin.math.abs

class MatrixImpl(override val defineType: DefineType,
                 override val data: MatrixData) : Matrix, Cloneable {

	constructor(data: MatrixData) : this(DefineType.ROW, data)

	private val rowDefine = defineType == DefineType.ROW

	override val row: Int = if (rowDefine) data.size else data.first().size

	override val column: Int = if (!rowDefine) data.size else data.first().size

	override val isSquare: Boolean = row == column

	override val dimension: Int = if (!isSquare) -1 else row

	override fun get(row: Int, column: Int): Double =
			if (rowDefine) data[row][column] else data[column][row]

	init {
		if (row == 0 || column == 0) throw IllegalArgumentException("不能构虚无矩阵")
		if ((rowDefine && data.any { it.size != column }) ||
				(!rowDefine && data.any { it.size != row }))
			throw IllegalArgumentException("矩阵参数错误")
	}

	override fun plus(other: Matrix): Matrix {
		checkDimension(other)
		return data.indices.map { r -> data[r].indices.map { c -> this[r, c] + other[r, c] } }.let {
			MatrixImpl(defineType, it)
		}
	}

	override fun minus(other: Matrix): Matrix {
		checkDimension(other)
		return data.indices.map { r -> data[r].indices.map { c -> this[r, c] - other[r, c] } }.let {
			MatrixImpl(defineType, it)
		}
	}

	override fun times(k: Double): Matrix = MatrixImpl(defineType, data.map { it.map { it * k } })

	override fun times(other: Matrix): Matrix = List(row) { r ->
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

	override fun div(k: Double): Matrix = MatrixImpl(defineType, data.map { it.map { it / k } })

	@Deprecated("没卵用")
	override infix fun pow(n: Int): Matrix {
		if (!isSquare) throw IllegalStateException("方阵才能乘方")
		if (n <= 0) throw IllegalArgumentException("至少乘一次方")

		return (1 until n).fold(this as Matrix) { acc, _ ->
			acc * this
		}
	}

	override fun elementaryTransformation(block: ElementaryTransformation.() -> Unit) =
			ElementaryTransformationImpl(data).apply(block).getResult()


	override fun companion(): Matrix {
		if (!isSquare) throw IllegalStateException("方阵才有伴随矩阵")
		return toDeterminant().let {
			List(row) { r ->
				List(column) { c ->
					it.getAlgebraCofactor(r, c)
				}
			}
		}.toMatrix(DefineType.COLUMN)
	}

	override fun transpose(): Matrix = MatrixImpl(when (defineType) {
		DefineType.COLUMN -> DefineType.ROW
		DefineType.ROW    -> DefineType.COLUMN
	}, data)

	override fun inverse(): Matrix {
		if (!isSquare) throw IllegalStateException("不方不能求逆")
		val d = toDeterminant().value
		if (d == .0) throw IllegalStateException("行列式为零不能求逆")
		return companion() / d
	}

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
				val parity = dL % 2 == 0
				dL /= 2
				val right: Int = dL
				val left: Int = if (parity) dL else dL + 1
				append(" ".repeat(left))
				append(" $data ")
				append(" ".repeat(right))
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
		return other.data == data && defineType == other.defineType
	}

	override fun hashCode(): Int {
		var result = defineType.hashCode()
		result = 31 * result + data.hashCode()
		return result
	}
}