package matrix.determinant.impl

import matrix.Matrix
import matrix.determinant.Determinant
import matrix.impl.MatrixImpl
import matrix.util.impl.MutableMatrixDataUtil

class DeterminantImpl(matrix: Matrix) : Determinant, Matrix by matrix {
	init {
		if (!isSquare) throw IllegalStateException("行列式必须是方的")
	}

	override val value: Double by lazy {
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

	override fun getCofactor(row: Int, column: Int): Determinant {
		if (row !in 0 until this.row) IllegalArgumentException("行数错误")
		if (column !in 0 until this.column) IllegalArgumentException("列数错误")
		return MutableMatrixDataUtil(data).apply {
			removeRow(row)
			removeColumn(column)
		}.getData().let(::MatrixImpl).let(::DeterminantImpl)
	}

	override fun getAlgebraCofactor(row: Int, column: Int): Double =
			(if ((row + column) % 2 == 0) 1 else -1) * getCofactor(row, column).value

	override fun toString(): String = buildString {
		append(" ".repeat(dimension / 2))
		appendln("Determinant ($dimension)")
		data.forEachIndexed { r, e ->
			append("│")
			e.indices.forEach { c ->
				append(" ${this@DeterminantImpl[r, c]} ")
				if (c == column - 1) append("│")
			}
			appendln()
		}
	}

	override fun equals(other: Any?): Boolean {
		if (other !is Determinant) return false
		return other.data == data && defineType == other.defineType
	}

	override fun hashCode(): Int {
		var result = defineType.hashCode()
		result = 31 * result + data.hashCode()
		return result
	}
}