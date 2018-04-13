package matrix.determinant.impl

import matrix.Matrix
import matrix.determinant.Determinant
import matrix.toMatrix
import matrix.transformation.util.impl.operateMatrixDataMutable
import kotlin.math.abs

class DeterminantImpl(matrix: Matrix) : Determinant, Matrix by matrix {

	init {
		if (!isSquare) throw IllegalStateException("行列式必须是方的")
	}

	private val value: Double by lazy {
		when (dimension) {
			1    -> this[0, 0]

			2    -> this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0]

			3    -> (this[0, 0] * this[1, 1] * this[2, 2]
					+ this[1, 0] * this[2, 1] * this[0, 2]
					+ this[2, 0] * this[0, 1] * this[1, 2]
					- this[0, 2] * this[1, 1] * this[2, 0]
					- this[1, 2] * this[2, 1] * this[0, 0]
					- this[2, 2] * this[0, 1] * this[1, 0])

			else -> (0 until column).sumByDouble { c ->
				this[0, c] * getAlgebraCofactor(0, c)
			}
		}
	}

	override fun calculate(): Double = value

	override fun getCofactor(row: Int, column: Int): Determinant {
		if (row !in 0 until this.row) IllegalArgumentException("行数错误")
		if (column !in 0 until this.column) IllegalArgumentException("列数错误")
		return operateMatrixDataMutable(data) {
			removeRow(row)
			removeColumn(column)
		}.toMatrix().toDeterminant()
	}

	override fun getAlgebraCofactor(row: Int, column: Int): Double =
			(if ((row + column) % 2 == 0) 1 else -1) * getCofactor(row, column).calculate()

	override fun toString(): String = buildString {
		val maxDataLength = data.flatten().map { it.toString().length }.max()!!
		append(" ".repeat(maxDataLength * column / 2))
		appendln("Determinant ($dimension)")
		data.forEachIndexed { r, e ->
			append("│")
			e.indices.forEach { c ->
				val data = this@DeterminantImpl[r, c]
				var dL = abs(maxDataLength - data.toString().length)
				dL /= 2
				val left: Int = if (dL % 2 == 0) dL else dL + 1
				append(" ".repeat(left))
				append(" $data ")
				append(" ".repeat(dL))
				if (c == column - 1) append("│")
			}
			appendln()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (other !is Determinant) return false
		return other.data == data
	}

	override fun hashCode(): Int = data.hashCode() + javaClass.hashCode()

}