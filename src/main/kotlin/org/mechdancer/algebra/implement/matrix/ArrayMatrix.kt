package org.mechdancer.algebra.implement.matrix

import org.mechdancer.algebra.contentEquals
import org.mechdancer.algebra.core.Determinant
import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.checkElementsEquals
import org.mechdancer.algebra.function.matrix.checkSameSize
import org.mechdancer.algebra.function.matrix.determinantValue
import org.mechdancer.algebra.function.matrix.rankDestructive
import org.mechdancer.algebra.hash
import org.mechdancer.algebra.implement.vector.toListVector

/**
 * [Matrix] of [DoubleArray]
 * 基于数组实现的矩阵
 * 值可变，线程不安全
 */
class ArrayMatrix(override val column: Int, val data: DoubleArray)
    : Determinant() {

    override fun updateRank() = clone().rankDestructive()
    override fun updateDet() = determinantValue()

    init {
        require(data.size % column == 0)
    }

    override val row = data.size / column

    private fun index(r: Int, c: Int) = r * column + c
    override operator fun get(r: Int, c: Int) = data[index(r, c)]
    override operator fun set(r: Int, c: Int, value: Double) {
        disable()
        data[index(r, c)] = value
    }

    override fun row(r: Int) = data.copyOfRange(r * column, (r + 1) * column).toList().toListVector()
    override fun column(c: Int) = (0 until row).map { get(it, c) }.toListVector()

    override val rows get() = (0 until row).map(::row)
    override val columns get() = (0 until column).map(::column)

    override fun setRow(r: Int, vector: List<Double>) {
        assert(vector.size == column)
        disable()
        vector.forEachIndexed { c, value -> set(r, c, value) }
    }

    override fun setColumn(c: Int, vector: List<Double>) {
        assert(vector.size == row)
        disable()
        vector.forEachIndexed { r, value -> set(r, c, value) }
    }

    override fun timesRow(r: Int, k: Double) {
        if (k == 1.0) return
        super.timesRow(r, k)
        for (i in index(r, 0) until index(r + 1, 0))
            data[i] *= k
    }

    override fun plusToRow(k: Double, r0: Int, r1: Int) {
        if (k == .0) return
        val difference = (r1 - r0) * column
        for (i in index(r0, 0) until index(r0 + 1, 0))
            data[i + difference] += k * data[i]
    }

    override fun exchangeRow(r0: Int, r1: Int) {
        if (r0 == r1) return
        super.exchangeRow(r0, r1)
        val temp = data.copyOfRange(index(r0, 0), index(r0 + 1, 0))
        System.arraycopy(data, index(r1, 0), data, index(r0, 0), column)
        System.arraycopy(temp, 0, data, index(r1, 0), column)
    }

    override fun timesColumn(c: Int, k: Double) {
        if (k == 1.0) return
        super.timesColumn(c, k)
        var i = c
        for (r in 0 until row) {
            data[i] *= k
            i += column
        }
    }

    override fun plusToColumn(k: Double, c0: Int, c1: Int) {
        if (k == .0) return
        var i0 = c0
        var i1 = c1
        for (r in 0 until row) {
            data[i0] += k * data[i1]
            i0 += column
            i1 += column
        }
    }

    override fun exchangeColumn(c0: Int, c1: Int) {
        if (c0 == c1) return
        super.exchangeColumn(c0, c1)
        (0 until row).forEach { r ->
            val temp = get(r, c0)
            set(r, c0, get(r, c1))
            set(r, c1, temp)
        }
    }

    override fun equals(other: Any?) =
        this === other
        || (other is Matrix
            && checkSameSize(this, other)
            && when (other) {
                is ListMatrix  -> data contentEquals other.data
                is ArrayMatrix -> data contentEquals other.data
                else           -> checkElementsEquals(this, other)
            })

    override fun hashCode() = hash(column, data)
    override fun toString() = matrixView("$row x $column Matrix")
    override fun clone() = ArrayMatrix(column, data)
}
