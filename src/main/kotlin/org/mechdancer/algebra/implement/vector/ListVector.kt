package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.contentEquals
import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.core.columnView
import kotlin.math.sqrt

/**
 * Vector of List of Double
 * 用浮点列表实现的向量
 */
class ListVector(val data: List<Double>) : Vector {
    override val dim get() = data.size
    override fun get(i: Int) = data[i]
    override val length by lazy { sqrt(data.sumByDouble { it * it }) }
    override fun toList() = data

    override fun equals(other: Any?) =
        this === other
            || (other is Vector
            && other.dim == dim
            && data contentEquals other.toList())

    override fun hashCode() = data.hashCode()
    override fun toString() = columnView()
}
