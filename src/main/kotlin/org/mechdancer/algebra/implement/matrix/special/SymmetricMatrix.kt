package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.contentEquals
import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.core.matrixView
import org.mechdancer.algebra.function.matrix.*
import org.mechdancer.algebra.implement.matrix.builder.toArrayMatrix
import org.mechdancer.algebra.implement.vector.toListVector
import kotlin.math.sqrt

/** 特化的对称矩阵 */
class SymmetricMatrix(val data: List<Double>) : SquareMatrix {
    override val dim: Int

    init {
        require(data.isNotEmpty())
        val temp = 8 * data.size + 1
        val delta = sqrt(temp.toDouble()).toInt()
        require(temp == delta * delta)
        require(delta % 2 == 1)
        dim = (delta - 1) / 2
    }

    override fun get(r: Int, c: Int): Double =
        when {
            r >= c -> data[r * (r + 1) / 2 + c]
            else   -> data[c * (c + 1) / 2 + r]
        }

    override val rows = (0 until dim).map(::row)
    override val columns get() = (0 until dim).map(::column)

    override fun row(r: Int) = (0 until dim).map { get(r, it) }.toListVector()
    override fun column(c: Int) = (0 until dim).map { get(it, c) }.toListVector()

    override val rank by lazy { toArrayMatrix().rankDestructive() }
    override val det by lazy { determinantValue() }
    override val trace by lazy { traceValue() }

    override fun equals(other: Any?) =
        this === other
        || (other is Matrix
            && checkSameSize(this, other)
            && when (other) {
                is SymmetricMatrix -> data contentEquals other.data
                else               -> checkElementsEquals(this, other)
            })

    override fun hashCode() = data.hashCode()

    override fun toString() = matrixView()
}
