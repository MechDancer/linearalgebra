package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.vector.normalize
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.toRad
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * 三维位姿（三维里程）
 * @param p 位置
 * @param d 方向
 */
data class Pose3D(
    val p: Vector3D,
    val d: Vector3D
) {
    /*
     存储方案是这样设计的：
     p 表示位置，没什么可说的；
     d 的方向表示姿态的旋转轴，长度则是绕此轴转动的弧度的一半；
     这是为了方便将姿态转换到四元数形式，来进行变换；
     若没有进行任何旋转，则 d 是一个零向量；
     */

    /** 旋转轴 */
    val u: Vector3D get() = d.normalize()

    /** 转角 */
    val theta: Angle get() = (2 * d.length).toRad()

    /** 增量 [delta] 累加到里程 */
    infix fun plusDelta(delta: Pose3D): Pose3D {
        val (v0, q0) = toQuaternions()
        val (v1, q1) = delta.toQuaternions()
        return pose3D(v0 + q0 * v1 * q0.conjugate,
                      q1 * q0)
    }

    /** 里程回滚到增量 [delta] 之前 */
    infix fun minusDelta(delta: Pose3D): Pose3D {
        val (v0, q0) = toQuaternions()
        val (v1, q1) = delta.toQuaternions()
        val qi = q1.conjugate * q0
        return pose3D(v0 - qi * v1 * qi.conjugate, qi)
    }

    /** 计算里程从标记 [mark] 到当前状态的增量 */
    infix fun minusState(mark: Pose3D): Pose3D {
        val (v0, q0) = mark.toQuaternions()
        val (v1, q1) = toQuaternions()
        return pose3D(q0.conjugate * (v1 - v0) * q0,
                      q1 * q0.conjugate)
    }

    override fun toString() = "p = ${p.simpleView()}, u = ${u.simpleView()}, θ = $theta"

    private companion object {
        fun Vector3D.simpleView() = "($x, $y, $z)"

        fun Pose3D.toQuaternions(): Pair<Quaternion, Quaternion> {
            val (x, y, z) = p
            val a = cos(d.length)
            val (b, c, d) = d.normalize() * sin(d.length)
            return Quaternion(.0, x, y, z) to Quaternion(a, b, c, d)
        }

        fun pose3D(p: Quaternion, d: Quaternion): Pose3D {
            assert(doubleEquals(p.r, .0))
            return Pose3D(p.v, d.v.run { normalize() * atan2(length, d.r) })
        }
    }
}
