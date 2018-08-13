package cn.berberman.algebra.vector.impl

import cn.berberman.algebra.vector.Axis3D
import cn.berberman.algebra.vector.Vector

class Vector2D(x: Double, y: Double) : VectorImpl(listOf(x, y)) {
	override val dimension: Int = 2

	operator fun component1() = this[Axis3D.X]

	operator fun component2() = this[Axis3D.Y]

	operator fun get(axis3D: Axis3D) = when (axis3D) {
		Axis3D.X -> this[0]
		Axis3D.Y -> this[1]
		else     -> throw IllegalArgumentException("维度参数错误")
	}

	companion object {
		fun to2D(vector: Vector) = vector as? Vector2D
				?: Vector2D(vector.getOrElse(0) { .0 }
						, vector.getOrElse(1) { .0 })
	}
}
