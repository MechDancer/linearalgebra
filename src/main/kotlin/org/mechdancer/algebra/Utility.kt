package org.mechdancer.algebra

/**
 * 双精度浮点的精度
 */
const val DOUBLE_PRECISION = 5E-15

// 精度范围
private val DOUBLE_EQUALS_RANGE = -DOUBLE_PRECISION..DOUBLE_PRECISION

/**
 * 宽条件判断浮点相等
 */
fun doubleEquals(a: Double, b: Double) = a - b in DOUBLE_EQUALS_RANGE

/**
 * 判断一个可迭代集中所有元素存在相等关系
 */
fun <T> Iterable<T>.isUnique(): Boolean = distinct().size == 1

/**
 * 判断一个可迭代集中所有元素存在某种相等关系
 */
infix fun <T, U> Iterable<T>.equalsOn(block: (T) -> U): Boolean =
	map(block).isUnique()

/**
 * 总结一个可迭代集中所有元素的特征
 */
fun <T> Iterable<T>.uniqueValue(): T? =
	distinct()
		.takeIf { it.size == 1 }
		?.first()

/**
 * 总结一个可迭代集中所有元素的某项特征
 */
infix fun <T, U> Iterable<T>.uniqueValue(block: (T) -> U): U? =
	map(block).uniqueValue()



