package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.ValueMutableMatrix
import org.mechdancer.algebra.doubleEquals
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
 * Multiply [k] with all elements
 * 原地数乘
 */
operator fun ValueMutableMatrix.timesAssign(k: Number) {
    val arg = k.toDouble()
    forEachAssign { it * arg }
}

/**
 * Divide all elements by [k]
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
 * Plus a matrix [other]
 * 原地加上另一个矩阵
 */
operator fun ValueMutableMatrix.plusAssign(other: Matrix) =
    zipAssign(other) { a, b -> a + b }

/**
 * Minus a matrix [other]
 * 原地减去另一个矩阵
 */
operator fun ValueMutableMatrix.minusAssign(other: Matrix) =
    zipAssign(other) { a, b -> a - b }

/**
 * Transpose (Square only)
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
 * Transform to row echelon matrix using elementary transformation,
 * and apply this transformation to [other]
 * @return impact on its determinate value
 *
 * 原地通过行初等变换变为行阶梯型阵
 * 对 [other] 应用同样变换
 * @return 变换导致行列式值的变化的倍数
 */
fun ValueMutableMatrix.rowEchelonAssignWith(
    other: Iterable<ValueMutableMatrix>
): Double {
    // 对接近零的数字规范化
    forEachAssign { if (doubleEquals(it, .0)) .0 else it }
    // 记录交换次数
    var sum = 0
    // 固定行数
    var fixed = 0
    // 按列化简
    for (c in 0 until column) {
        // 从此列所有未固定的行中找到第一个非零元素，全为零则直接查找下一列
        val i = (fixed until row).firstOrNull { r -> get(r, c) != .0 } ?: continue
        // 将其所在行交换到未固定的首行
        if (i != fixed) {
            ++sum
            exchangeRow(i, fixed)
            other.forEach { it.exchangeRow(i, fixed) }
        }

        // 取出这一行的首元
        val head = get(fixed, c)
        // 用首元将此列未固定的其他元素化为零
        for (r in fixed + 1 until row) {
            val k = -get(r, c) / head
            plusToRow(k, fixed, r)
            other.forEach { it.plusToRow(k, fixed, r) }
            // 强保证化为零有效
            set(r, c, .0)
        }

        // 固定行数加一
        ++fixed
    }
    return if (sum % 2 == 0) 1.0 else -1.0
}

/**
 * Transform to the simplest row echelon matrix using elementary transformation,
 * and apply this transformation to [other]
 * @return impact on its determinate value
 *
 * 原地通过行初等变换变为最简行阶梯型阵
 * 对 [other] 应用同样变换
 * @return 变换导致行列式值的变化的倍数
 */
fun ValueMutableMatrix.simplifyAssignWith(
    other: Iterable<ValueMutableMatrix>
): Double {
    var scale = rowEchelonAssignWith(other)

    var c = column
    // 清零上三角区
    for (r in (0 until row).reversed()) {
        // 找到本行最后一个非零元素
        var tail: Double
        do {
            tail = get(r, --c)
        } while (c >= 0 && tail == .0)

        // 在此进行 0 约化会导致 40 阶希尔伯特矩阵求逆的测试无法通过
        // do {
        //     tail = get(r, --c)
        //     if (!doubleEquals(tail, .0)) break
        //     // 接近 0 视作 0
        //     set(r, c, .0)
        // } while (c >= 0)

        // 有全零行，不再继续化简
        if (c < 0) break

        // 用首元将此列未固定的其他元素化为零
        for (t in (0 until r)) {
            val k = -get(t, c) / tail
            plusToRow(k, r, t)
            other.forEach { it.plusToRow(k, r, t) }
            // 强保证化为零有效
            set(t, c, .0)
        }
    }

    c = 0
    // 主对角线归一化
    for (r in 0 until row)
        while (c < column) {
            val item = get(r, c++)
            if (doubleEquals(item, .0)) {
                set(r, c - 1, .0)
                continue
            }

            val k = 1 / item
            scale *= k
            timesRow(r, k)
            other.forEach { it.timesRow(r, k) }
            break
        }

    return scale
}

/**
 * Transform to row echelon matrix using elementary transformation,
 * and apply this transformation to [other]
 * @return impact on its determinate value
 *
 * 原地通过行初等变换变为行阶梯型阵
 * 对 [other] 应用同样变换
 * @return 变换导致行列式值的变化的倍数
 */
fun ValueMutableMatrix.rowEchelonAssignWith(
    vararg other: ValueMutableMatrix
) = rowEchelonAssignWith(other.toList())

/**
 * Transform to the simplest row echelon matrix using elementary transformation,
 * and apply this transformation to [other]
 * @return impact on its determinate value
 *
 * 原地通过行初等变换变为最简行阶梯型阵
 * 对 [other] 应用同样变换
 * @return 变换导致行列式值的变化的倍数
 */
fun ValueMutableMatrix.simplifyAssignWith(
    vararg other: ValueMutableMatrix
) = simplifyAssignWith(other.toList())

/**
 * Transform to row echelon matrix using elementary transformation
 * @return impact on its determinate value
 *
 * 原地通过行初等变换变为行阶梯型阵
 * @return 变换导致行列式值的变化的倍数
 */
fun ValueMutableMatrix.rowEchelonAssign() = rowEchelonAssignWith()

/**
 * Transform to the simplest row echelon matrix using elementary transformation
 * @return impact on its determinate value
 *
 * 原地通过行初等变换变为最简行阶梯型阵
 * @return 变换导致行列式值的变化的倍数
 */
fun ValueMutableMatrix.simplifyAssign() = simplifyAssignWith()
