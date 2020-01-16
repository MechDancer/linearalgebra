package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.function.vector.normalize
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.toRad

/**
 * 三维位姿（三维里程）
 * @param p 位置
 * @param d 方向
 */
data class Pose3D(
    val p: Vector3D,
    val d: Vector3D
) {
    val u: Vector3D by lazy { d.normalize() }
    val theta: Angle by lazy { d.length.toRad() }

    override fun toString() = "p = ${p.simpleString()}, u = ${u.simpleString()}, θ = $theta"

    companion object {
        private fun Vector3D.simpleString() = "($x, $y, $z)"
    }
}
