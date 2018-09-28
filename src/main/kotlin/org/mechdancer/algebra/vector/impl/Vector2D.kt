package org.mechdancer.algebra.vector.impl

import org.mechdancer.algebra.vector.Vector

/** 二维向量 */
class Vector2D(x: Double, y: Double) : VectorImpl(listOf(x, y)) {
	override val dimension: Int = 2

	/** 复制并进行指定的修改 */
	fun copy(x: Double? = null, y: Double? = null) =
		Vector2D(x ?: this[0], y ?: this[1])

	companion object {
		/** 转到[Vector2D]的实例 */
		operator fun invoke(vector: Vector) =
			vector as? Vector2D
				?: (vector as VectorImpl).run {
					Vector2D(getOrElse(0, .0), getOrElse(1, .0))
				}
	}
}
