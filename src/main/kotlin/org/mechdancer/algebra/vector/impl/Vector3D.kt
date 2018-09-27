package org.mechdancer.algebra.vector.impl

import org.mechdancer.algebra.vector.Vector

/** 三维向量 */
class Vector3D(x: Double, y: Double, z: Double) : VectorImpl(listOf(x, y, z)) {
	override val dimension: Int = 3

	/** 复制并进行指定的修改 */
	fun copy(x: Double? = null, y: Double? = null, z: Double? = null) =
		Vector3D(x ?: this[0], y ?: this[1], z ?: this[2])

	/** 叉乘（向量积） */
	infix fun cross(other: Vector3D): Vector3D =
		Vector3D(
			this[1] * other[2] - this[2] * other[1],
			this[2] * other[0] - this[0] * other[2],
			this[0] * other[1] - this[1] * other[0])

	companion object {
		/** 转到[Vector3D]的实例 */
		operator fun invoke(vector: Vector) =
			vector as? Vector3D
				?: (vector as VectorImpl).run {
					Vector3D(
						vector.getOrElse(0) { .0 },
						vector.getOrElse(1) { .0 },
						vector.getOrElse(2) { .0 })
				}
	}
}
