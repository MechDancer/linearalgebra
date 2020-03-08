package org.mechdancer.algebra.implement.matrix

import org.mechdancer.algebra.contentEquals
import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.hash
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.toListVector

/**
 * 基于列表实现的矩阵
 */
class ListMatrix(
    override val column: Int,
    val data: List<Double>
) : Matrix {
    init {
        require(data.size % column == 0)
    }

    override val row = data.size / column
    override fun get(r: Int, c: Int) = data[r * column + c]

    override fun row(r: Int) = data.subList(r * column, (r + 1) * column).toListVector()
    override fun column(c: Int) = (0 until row).map { get(it, c) }.toListVector()

    override val rows get() = (0 until row).map(::row)
    override val columns get() = (0 until column).map(::column)

    override val rank by lazy { toArrayMatrix().rankDestructive() }
    override val det by lazy { determinantValue() }
    override val trace by lazy { traceValue() }

    override fun equals(other: Any?) =
        this === other
        || (other is Matrix
            && checkSameSize(this, other)
            && when (other) {
                is ListMatrix  -> other.data contentEquals data // 看似完全相同
                is ArrayMatrix -> other.data contentEquals data // 其实是重载函数，不可省略
                else           -> checkElementsEquals(this, other)
            })

    override fun hashCode() = hash(column, data)

    override fun toString() = matrixView("$row x $column Matrix")
}
