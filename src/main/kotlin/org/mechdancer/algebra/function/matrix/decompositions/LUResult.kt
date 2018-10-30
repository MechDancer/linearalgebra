package org.mechdancer.algebra.function.matrix.decompositions

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
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
		 *
		 * Use a "left-looking", dot-product, Crout/Doolittle algorithm.
		 */
		operator fun invoke(matrix: Matrix): LUResult {
			// 存储数值到二维数组
			val lu = Array(matrix.row) { r ->
				DoubleArray(matrix.column) { c -> matrix[r, c] }
			}

			val m = matrix.row
			val n = matrix.column

			val pivot = IntArray(m) { it }
			var pivotSign = 1

			// 开辟空间以存储一列
			val jCol = DoubleArray(m)

			// 对每一列变换
			for (j in 0 until n) {
				// 创建第 j 列的副本以加快访问速度
				// 真的快！影响极其重大！
				for (i in 0 until m) jCol[i] = lu[i][j]
				// 对每一行变换
				lu.forEachIndexed { i, iRow ->
					// Most of the time is spent in the following dot product.
					// 这个点乘是最耗时的操作
					// 也是完全无法理解的操作

					// 性能对比：var > sumByDouble > fold >> zip
					// 很奇怪，sumByDouble 明明就是用 var 实现的，而且还 inline 了，但是性能确实可见地低于直接写 var
					// zip 使用了 ArrayList 实现，按理说不应该，但是实际慢到了无法测试的程度
					// jCol[i] -= (0 until min(i, j)).sumByDouble { k -> iRow[k] * jCol[k] }
					// jCol[i] -= (0 until min(i, j)).fold(.0) { acc, k -> acc + iRow[k] * jCol[k] }
					// jCol[i] -= iRow.zip(jCol) { a, b -> a * b }.sum()
					jCol[i] -= run { var s = .0; for (k in 0 until min(i, j)) s += iRow[k] * jCol[k]; s }
					iRow[j] = jCol[i]
				}

				// Find pivot and exchange if necessary.
				// 找到主轴并修改排序
				// 我甚至不知道主轴是什么东西
				// 线索：经测试，主轴表示了行的顺序
				//      正常情况下分解后 A = L * U
				//      但按此算法分解后需要将 L * U 的积矩阵按主轴指示的顺序重排才能得到原矩阵
				//      恐怕这是算法性能优势的一大原因
				(j until m)
					.maxBy { abs(jCol[it]) }
					?.takeIf { it != j }
					?.let { k ->
						// 交换 缓存的 k行 和 j行
						// 交换 主轴的 k项 和 j项
						// 并给主轴符号取反

						lu[k].let {
							lu[k] = lu[j]
							lu[j] = it
						}

						pivot[k].let {
							pivot[k] = pivot[j]
							pivot[j] = it
						}

						pivotSign = -pivotSign
					}

				// L矩阵元素除以主对角线化为单位下三角阵
				lu[j][j]
					.takeIf { it != .0 }
					?.let { for (i in j + 1 until m) lu[i][j] /= it }
			}

			return LUResult(lu, pivot.toList(), pivotSign.toByte())
		}
	}
}
