package org.mechdancer.algebra.function.matrix.decompositions

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import org.mechdancer.algebra.implement.matrix.special.DiagonalMatrix
import kotlin.math.abs
import kotlin.math.min

/**
 * 存储和使用 LU 变换的结果
 * @param LU        LU分解的解矩阵，由上三角矩阵和单位下三角矩阵组成
 * @param pivot     全为整数的主轴
 * @param pivotSign 主轴符号，用于计算行列式
 */
class LUResult
private constructor(
	private val LU: Array<DoubleArray>,
	val pivot: List<Int>,
	private val pivotSign: Byte
) {
	val m get() = LU.size
	val n get() = LU[0].size

	val singularity =
		(0 until n).any { i -> LU[i][i] == .0 }

	val u
		get() = listMatrixOf(m, n) { r, c ->
			when {
				r <= c -> LU[r][c]
				else   -> .0
			}
		}

	val d
		get() = DiagonalMatrix(pivot.map(Number::toDouble))

	val l
		get() = listMatrixOf(m, n) { r, c ->
			when {
				r > c -> LU[r][c]
				r < c -> .0
				else  -> 1.0
			}
		}

	val det
		get() =
			if (m != n) null
			else (0 until n).fold(pivotSign.toDouble()) { acc, i -> acc * LU[i][i] }

	companion object {
		/**
		 * This function is refactor from Jama.
		 */
		operator fun invoke(matrix: Matrix): LUResult {
			@Suppress("LocalVariableName")
			val LU = Array(matrix.row) { r ->
				DoubleArray(matrix.column) { c -> matrix[r, c] }
			}

			val m = matrix.row
			val n = matrix.column

			val pivot = IntArray(m) { it }
			var pivotSign = 1

			// Use a "left-looking", dot-product, Crout/Doolittle algorithm.
			// From Jama

			val jCol = DoubleArray(m)

			// Outer loop.
			// 主循环

			for (j in 0 until n) {

				// Make a copy of the j-th column to localize references.
				// 创建第 j 列的副本以加快访问速度
				for (i in 0 until m) jCol[i] = LU[i][j]

				// Apply previous transformations.
				// 进行预变换

				LU.forEachIndexed { i, iRow ->
					// Most of the time is spent in the following dot product.
					// 这个点乘是最耗时的操作
					jCol[i] -= (0 until min(i, j)).sumByDouble { k -> iRow[k] * jCol[k] }
					iRow[j] = jCol[i]
				}

				// Find pivot and exchange if necessary.
				// 找到主轴

				val p = (j until m).maxBy { abs(jCol[it]) } ?: 0
				if (p != j) {
					// 交换 LU 缓存的 p行 和 j行
					// 再交换主轴的 p项 和 j项

					for (k in 0 until n) {
						val temp = LU[p][k]
						LU[p][k] = LU[j][k]
						LU[j][k] = temp
					}

					// 交换
					val temp = pivot[p]
					pivot[p] = pivot[j]
					pivot[j] = temp
					pivotSign = -pivotSign
				}

				// Compute multipliers.

				val scale = LU[j][j]
				if (j < m && scale != .0)
					for (i in j + 1 until m)
						LU[i][j] /= scale
			}

			return LUResult(LU, pivot.toList(), pivotSign.toByte())
		}
	}
}
