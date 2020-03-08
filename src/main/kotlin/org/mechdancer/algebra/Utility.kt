package org.mechdancer.algebra

/**
 * 双精度浮点的精度
 */
internal const val DOUBLE_PRECISION = 1E-12

// 精度范围
internal val DOUBLE_EQUALS_RANGE = -DOUBLE_PRECISION..DOUBLE_PRECISION

/**
 * 宽条件判断浮点相等
 */
fun doubleEquals(a: Double, b: Double) = a - b in DOUBLE_EQUALS_RANGE

/**
 * 判断两个浮点列表相同
 * 最好先判断维数相同
 */
infix fun Collection<Double>.contentEquals(other: Collection<Double>) =
    size == other.size && asSequence().zip(other.asSequence(), ::doubleEquals).all { true }

/**
 * 判断两个浮点列表相同
 * 最好先判断维数相同
 */
infix fun DoubleArray.contentEquals(other: Collection<Double>) =
    size == other.size && asSequence().zip(other.asSequence(), ::doubleEquals).all { true }

/**
 * 计算许多元素的哈希值
 * 慎用！有递归风险！
 */
fun hash(vararg elements: Any?) =
    elements.fold(0) { code, it -> code.hashCode() * 31 + (it?.hashCode() ?: 0) }

fun <T> Iterable<T>.isUnique() = toSet().size == 1
fun <T> Iterable<T>.uniqueValue(): T? = toSet().singleOrNull()
inline fun <T, U> Iterable<T>.uniqueOn(block: (T) -> U) = map(block).isUnique()
inline fun <T, U> Iterable<T>.uniqueValue(block: (T) -> U): U? = map(block).uniqueValue()

fun <T> Sequence<T>.isUnique() = toSet().size == 1
fun <T> Sequence<T>.uniqueValue(): T? = toSet().singleOrNull()
fun <T, U> Sequence<T>.uniqueOn(block: (T) -> U) = map(block).isUnique()
fun <T, U> Sequence<T>.uniqueValue(block: (T) -> U): U? = map(block).uniqueValue()

internal inline fun <T, U> List<T>.zipFast(other: List<T>, block: (T, T) -> U) =
    List(kotlin.math.min(size, other.size)) { block(this[it], other[it]) }
