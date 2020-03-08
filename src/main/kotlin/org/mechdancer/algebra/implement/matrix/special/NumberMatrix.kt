package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.checkSameSize
import org.mechdancer.algebra.function.matrix.filterIndexed
import org.mechdancer.algebra.hash
import org.mechdancer.algebra.implement.vector.toListVector
import kotlin.math.pow

/**
 * 不可变数值矩阵特化实现
 * @param dim   维数
 * @param value 数值
 */
class NumberMatrix
private constructor(
    override val dim: Int,
    val value: Double
) : SquareMatrix {
    init {
        require(dim > 0)
    }

    override fun get(r: Int, c: Int) = if (r == c) value else .0

    override val rows get() = List(row) { r -> List(column) { c -> get(r, c) }.toListVector() }
    override val columns get() = List(column) { c -> List(row) { r -> get(r, c) }.toListVector() }

    override fun row(r: Int) = List(column) { c -> get(r, c) }.toListVector()
    override fun column(c: Int) = List(row) { r -> get(r, c) }.toListVector()

    override val rank = if (value != .0) row else 0
    override val det = value.pow(dim)
    override val trace = value * dim

    override fun equals(other: Any?) =
        this === other
        || (other is Matrix
            && checkSameSize(this, other)
            && ((other as? NumberMatrix)?.value == value
                || other.filterIndexed { r, c, it -> it != if (r == c) value else .0 }.isEmpty()
               ))

    override fun hashCode() = hash(row, column, value)

    override fun toString() = matrixView()

    companion object {
        private val UnitOrder1 = NumberMatrix(1, 1.0)
        private val UnitOrder2 = NumberMatrix(2, 1.0)
        private val UnitOrder3 = NumberMatrix(3, 1.0)

        @JvmStatic
        operator fun get(dim: Int, value: Number) =
            value.toDouble()
                .let {
                    when (it) {
                        0.0  -> ZeroMatrix[dim]
                        1.0  -> when (dim) {
                            1    -> UnitOrder1
                            2    -> UnitOrder2
                            3    -> UnitOrder3
                            else -> NumberMatrix(dim, 1.0)
                        }
                        else -> NumberMatrix(dim, it)
                    }
                }
    }
}
