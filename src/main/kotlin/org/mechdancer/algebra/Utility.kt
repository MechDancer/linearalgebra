package org.mechdancer.algebra

/**
 * 双精度浮点的精度
 */
internal const val DOUBLE_PRECISION = 5E-15

// 精度范围
internal val DOUBLE_EQUALS_RANGE = -DOUBLE_PRECISION..DOUBLE_PRECISION

/**
 * 宽条件判断浮点相等
 */
fun doubleEquals(a: Double, b: Double) = a - b in DOUBLE_EQUALS_RANGE

/**
 * 判断布尔列表全为真
 */
fun Iterable<Boolean>.alwaysTrue() = all { it }

/**
 * 判断两个浮点列表相同
 * 最好先判断维数相同
 */
infix fun Iterable<Double>.contentEquals(other: Iterable<Double>) =
	zip(other, ::doubleEquals).alwaysTrue()

/**
 * 判断两个浮点列表相同
 * 最好先判断维数相同
 */
infix fun DoubleArray.contentEquals(other: List<Double>) =
	zip(other, ::doubleEquals).alwaysTrue()

/**
 * 计算许多元素的哈希值
 * 慎用！有递归风险！
 */
fun hash(vararg elements: Any?) =
	elements.fold(0) { code, it -> code.hashCode() * 31 + (it?.hashCode() ?: 0) }

/**
 * 判断一个可迭代集中所有元素存在相等关系
 */
fun <T> Iterable<T>.isUnique() = toSet().size == 1

/**
 * 判断一个可迭代集中所有元素存在某种相等关系
 */
infix fun <T, U> Iterable<T>.uniqueOn(block: (T) -> U) = map(block).isUnique()

/**
 * 总结一个可迭代集中所有元素的特征
 */
fun <T> Iterable<T>.uniqueValue(): T? = toSet().singleOrNull()

/**
 * 总结一个可迭代集中所有元素的某项特征
 */
infix fun <T, U> Iterable<T>.uniqueValue(block: (T) -> U): U? = map(block).uniqueValue()

/**
 * 计时
 * 用于性能测试
 */
internal fun timer(block: () -> Any?): Long =
	System.nanoTime()
		.also { block() }
		.let { System.nanoTime() - it }
