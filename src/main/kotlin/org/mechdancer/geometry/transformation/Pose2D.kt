package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.function.vector.unaryMinus
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.to2D
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.adjust
import org.mechdancer.geometry.angle.rotate
import org.mechdancer.geometry.angle.unaryMinus

/**
 * 二维位姿（二维里程）
 * @param p 位置
 * @param d 方向
 */
data class Pose2D(val p: Vector2D, val d: Angle) :
    Transformation<Pose2D>, Odometry<Pose2D> {
    override val dim: Int get() = 2

    override fun times(p: Vector): Vector2D {
        require(p.dim == 2)
        return this.p + (p.to2D() rotate d)
    }

    override fun times(tf: Pose2D): Pose2D =
        Pose2D(p + (tf.p rotate d), d rotate tf.d)

    override fun inverse(): Pose2D =
        Pose2D(-p rotate -d, -d)

    override fun plusDelta(delta: Pose2D) =
        this * delta

    override fun minusDelta(delta: Pose2D) =
        this * delta.inverse()

    override fun minusState(mark: Pose2D) =
        mark.inverse() * this

    override fun toString() = "(${p.x}, ${p.y})($d)"
}
