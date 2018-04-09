package vector.impl

import vector.Axis3D
import vector.Vector

class Vector3D(x: Double, y: Double, z: Double) : VectorImpl(listOf(x, y, z)) {
	override val dimension: Int = 3

	operator fun get(axis3D: Axis3D) = when (axis3D) {
		Axis3D.X -> this[0]
		Axis3D.Y -> this[1]
		Axis3D.Z -> this[2]
	}

	infix fun cross(other: Vector3D): Vector3D =
			Vector3D(this[1] * other[2] - this[2] * other[1],
					this[2] * other[0] - this[0] * other[2],
					this[0] * other[1] - this[1] * other[0])


	infix fun x(other: Vector3D): Vector = this cross other

	companion object {
		fun to3D(vector: Vector): Vector3D = if (vector.dimension != 3)
			Vector3D(vector.getOrElse(0) { .0 },
					vector.getOrElse(1) { .0 },
					vector.getOrElse(2) { .0 })
		else vector as Vector3D
	}
}
