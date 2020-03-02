package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.vector.ListVector

/**
 * Copy a vector and change value of the first 3 dimension by their name
 * 复制一个向量，并基于前三个维度的名字来修改值
 *
 * @param x the 1st dimension
 * @param y the 2nd dimension
 * @param z the 3rd dimension
 */
fun Vector.copy(x: Number? = null, y: Number? = null, z: Number? = null): Vector {
    val map = mutableMapOf<Int, Double>()
    x?.toDouble()?.also { map += 0 to it }
    y?.toDouble()?.also { map += 1 to it }
    z?.toDouble()?.also { map += 2 to it }
    return List(dim) { map[it] ?: get(it) }.let(::ListVector)
}

/**
 * Copy a vector and change value by their index
 * 复制向量，并基于序号索引并进行指定的修改
 *
 * no exception will be thrown
 *   even a index which is not exist in this vector are mentioned
 * 试图指定不存在的序号并不会产生异常
 *
 * @param change pair of index and the new value
 */
fun Vector.copy(vararg change: Pair<Int, Number>): Vector {
    val map = change.associate { it }
    return List(dim) { map[it]?.toDouble() ?: get(it) }.let(::ListVector)
}

/**
 * Select some dimension of the vector, then copy them to a new vector
 * 从向量选取指定的维度组成新的向量
 */
fun Vector.select(vararg indices: Int): Vector =
    toList().filterIndexed { i, _ -> i in indices }.let(::ListVector)

/**
 * Select a range of index of the dimensions, then copy them to a new vector
 * 从向量选取指定的维度组成新的向量
 */
fun Vector.select(range: IntRange): Vector =
    toList().drop(range.first).take(range.last - range.first + 1).let(::ListVector)
