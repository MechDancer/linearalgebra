package org.mechdancer.algebra.function

/**
 * 判断一个可迭代集中所有元素存在某种相等关系
 */
infix fun <T, U> Iterable<T>.equalsOn(block: (T) -> U): Boolean =
	map(block).distinct().size == 1

/**
 * 总结一个可迭代集中所有元素的某项特征
 */
infix fun <T, U> Iterable<T>.uniqueValue(block: (T) -> U): U =
	map(block)
		.distinct()
		.also { assert(it.size == 1) }
		.first()
