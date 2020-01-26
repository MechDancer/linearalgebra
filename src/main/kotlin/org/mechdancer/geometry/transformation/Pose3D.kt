package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.vector.*
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.algebra.implement.vector.to3D
import org.mechdancer.algebra.implement.vector.vector3D
import org.mechdancer.algebra.implement.vector.vector3DOfZero
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.adjust
import org.mechdancer.geometry.angle.toRad
import kotlin.math.*

/**
 * 三维位姿（三维里程）
 * @param p 位置
 * @param d 方向
 */
data class Pose3D(val p: Vector3D, val d: Vector3D)
    : Transformation<Pose3D>, Odometry<Pose3D> {
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

    override val dim: Int get() = 3

    override fun times(p: Vector): Vector3D {
        require(p.dim == 3)
        return this.p + (p.to3D() rotateVector d)
    }

    override fun times(tf: Pose3D): Pose3D =
        Pose3D(p + (tf.p rotateVector d), d rotateAngle tf.d)

    override fun inverse(): Pose3D =
        Pose3D(-p rotateVector -d, -d)

    /** 增量 [delta] 累加到里程 */
    override fun plusDelta(delta: Pose3D) =
        this * delta

    /** 里程回滚到增量 [delta] 之前 */
    override fun minusDelta(delta: Pose3D) =
        this * delta.inverse()

    /** 计算里程从标记 [mark] 到当前状态的增量 */
    override fun minusState(mark: Pose3D) =
        mark.inverse() * this

    override fun toString() = "p = ${p.simpleView()}, u = ${u.simpleView()}, θ = $theta"

    infix fun equivalentWith(others: Pose3D): Boolean {
        if (this === others) return true
        if (p != others.p) return false
        if (d == others.d) return true
        val axis0 = d.normalize()
        val axis1 = others.d.normalize()
        val delta = abs(when (axis0) {
                            axis1  -> others.d.length
                            -axis1 -> others.d.length + PI / 2
                            else   -> return false
                        } - d.length)
        return (delta / PI).let { doubleEquals(it.toInt().toDouble(), it) }
    }

    private companion object {
        fun Vector3D.simpleView() = "($x, $y, $z)"

        fun Vector3D.asPosition() =
            quaternion(.0, this)

        fun Vector3D.asAngle() =
            when {
                length < 0   -> length + PI * (-length / PI).toInt() + PI
                length >= PI -> length - PI * (+length / PI).toInt()
                else         -> length
            }.let { half ->
                quaternion(cos(half), normalize() * sin(half))
            }

        infix fun Vector3D.rotateVector(d: Vector3D): Vector3D {
            val q = d.asAngle()
            return (q * asPosition() * q.conjugate).v
        }

        infix fun Vector3D.rotateAngle(d: Vector3D): Vector3D {
            val q = d.asAngle() * asAngle()
            val r = q.r
            val v = q.v
            return v.normalize() * atan2(v.length, r)
        }
    }
}
