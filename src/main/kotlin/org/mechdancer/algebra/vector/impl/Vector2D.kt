package org.mechdancer.algebra.vector.impl

import org.mechdancer.algebra.dimensionArgumentError
import org.mechdancer.algebra.vector.Axis3D
import org.mechdancer.algebra.vector.Vector

/** 二维向量 */
class Vector2D(x: Double, y: Double) : VectorImpl(listOf(x, y)) {
	override val dimension: Int = 2

	operator fun component1() = this[Axis3D.X]

	operator fun component2() = this[Axis3D.Y]

	operator fun get(axis3D: Axis3D) = when (axis3D) {
		Axis3D.X -> this[0]
		Axis3D.Y -> this[1]
		else     -> throw dimensionArgumentError
	}

	companion object {
		/** 转到[Vector2D]的实例 */
		fun to2D(vector: Vector) =
			vector as? Vector2D
				?: (vector as VectorImpl).run {
					Vector2D(getOrElse(0) { .0 }, getOrElse(1) { .0 })
				}
	}
}
