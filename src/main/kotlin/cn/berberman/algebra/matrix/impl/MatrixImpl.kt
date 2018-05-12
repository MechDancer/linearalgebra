package cn.berberman.algebra.matrix.impl

import cn.berberman.algebra.matrix.Matrix
import cn.berberman.algebra.matrix.MatrixData
import cn.berberman.algebra.matrix.MatrixElement
import cn.berberman.algebra.matrix.determinant.Determinant
import cn.berberman.algebra.matrix.determinant.impl.DeterminantImpl
import cn.berberman.algebra.matrix.toMatrix
import cn.berberman.algebra.matrix.transformation.ElementaryTransformation
import cn.berberman.algebra.matrix.transformation.impl.ElementaryTransformationImpl
import cn.berberman.algebra.matrix.transformation.util.impl.ImmutableMatrixDataUtil
import cn.berberman.algebra.matrix.transformation.util.impl.operateMatrixDataMutable
import cn.berberman.algebra.vector.Vector
import cn.berberman.algebra.vector.toVector
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

	private val inverseByCompanionLazy: Matrix by lazy {
		if (!isSquare) throw IllegalStateException("逆矩阵未定义")
		val d = det()
		if (d == .0) throw IllegalStateException("逆矩阵未定义")
		companion() / d
	}

	private val inverseByRowEchelonLazy: Matrix by lazy {
		if (!isSquare) throw IllegalStateException("逆矩阵未定义")
		operateMatrixDataMutable(withUnit().rowEchelon().data) {
			(0 until dimension).forEach {
				removeColumn(it)
			}
		}.toMatrix()
	}

	private val determinantLazy: Determinant by lazy { DeterminantImpl(this) }

	private val detLazy: Double by lazy {
		if (!isSquare) throw IllegalStateException("行列式未定义")
		toDeterminant().calculate()
	}

	private val rowEchelonLazy: Matrix by lazy {

		fun MutableList<MutableList<Double>>.simplify() {

			fun rowSwap(from: Int, to: Int) {
				val temp = this[from]
				this[from] = this[to]
				this[to] = temp
			}

			fun rowMultiply(r1: Int, n: Double) {
				//避免出现 -0.0 这样丑恶的数字
				this[r1].forEachIndexed { i, e -> this[r1][i] = if (e == .0) .0 else e * n }
			}

			fun rowAddTo(from: Int, to: Int, n: Double) {
				this[from].forEachIndexed { i, e -> this[to][i] = this[to][i] + e * n }
			}


			val position = mutableListOf<Int>()
			var index = 0

			var i = 0
			var k: Int

			for (j in this[0].indices) {
				k = i

				//找到第一个非零列的首个非零元素，将其所在行交换到当前首行
				while (k < this.size && this[k][j] == 0.0) {
					k++
				}
				if (k != this.size)
					rowSwap(k, i)
				else {
					i++
					continue
				}


				//储存主元位置，将主元化为1
				position.add(index++, j)
				rowMultiply(i, 1 / this[i][j])

				//如果到最后一行则退出
				if (i == size - 1)
					break

				//将主元所在列下方元素化为0
				for (d in i + 1 until size)
					if (this[d][j] != 0.0)
						rowAddTo(i, d, -this[d][j])


				i++
			}

			//从下到上化为最简阶梯形
			for (e in i downTo 1)
				for (s in e - 1 downTo 0)
					rowAddTo(e, s, -this[s][position[e]])


		}

		data.map { it.toMutableList() }
				.toMutableList().also { it.simplify() }.toMatrix()
	}

	private val withUnitLazy: Matrix by lazy {
		operateMatrixDataMutable(data) {
			if (!isSquare) throw IllegalStateException("此维度单位矩阵未定义")
			val unit = Matrix.unitOf(dimension)
			val unitColumns =
					(0 until unit.dimension).fold(mutableListOf<MatrixElement>()) { acc, e ->
						acc.apply { add(ImmutableMatrixDataUtil.splitColumn(unit.data, e)) }
					}
			unitColumns.forEach { addColumn(it) }
		}.toMatrix()
	}

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

	override fun div(other: Matrix): Matrix = this * other.inverseByCompanion()

	override fun div(k: Double): Matrix = data.map { it.map { it / k } }.toMatrix()

	override infix fun pow(n: Int): Matrix {
		if (!isSquare) throw IllegalStateException("乘方未定义")
		if (n <= 0) throw IllegalArgumentException("至少乘一次方")

		return (1 until n).fold(this as Matrix) { acc, _ ->
			acc * this
		}
	}

	override fun round(n: Int): Matrix = TODO("放弃")

	override fun toDeterminant(): Determinant = determinantLazy

	override fun det(): Double = detLazy

	override fun elementaryTransformation(block: ElementaryTransformation.() -> Unit) =
			ElementaryTransformationImpl(data).apply(block).getResult()


	override fun companion(): Matrix = companionLazy

	override fun transpose(): Matrix = transposeLazy

	override fun rowEchelon(): Matrix = rowEchelonLazy

	override fun withUnit(): Matrix = withUnitLazy

	override fun inverseByCompanion(): Matrix = inverseByCompanionLazy

	override fun inverseByRowEchelon(): Matrix = inverseByRowEchelonLazy

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
				append(" ".repeat(if (parity) dL else dL + 1))
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