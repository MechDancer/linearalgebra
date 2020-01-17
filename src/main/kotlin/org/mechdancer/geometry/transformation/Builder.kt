package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.*
import org.mechdancer.geometry.angle.toAngle
import org.mechdancer.geometry.angle.toRad
import org.mechdancer.geometry.angle.toVector
import kotlin.math.cos
import kotlin.math.sin

fun pose(x: Number = 0, y: Number = 0, theta: Number = 0) =
    Pose2D(vector2DOf(x, y), theta.toRad())

fun odometry(x: Number = 0, y: Number = 0, theta: Number = 0) =
    Pose2D(vector2DOf(x, y), theta.toRad())

fun quaternion(a: Number = 0, b: Number = 0, c: Number = 0, d: Number = 0) =
    Quaternion(a.toDouble(), b.toDouble(), c.toDouble(), d.toDouble())

fun quaternion(r: Number = 0, v: Vector3D = vector3DOfZero()) =
    Quaternion(r.toDouble(), v.x, v.y, v.z)

fun Transformation.toPose2D(): Pose2D {
    require(dim == 2) { "2d transformation is required" }
    val p = invoke(vector2DOfZero()).to2D()
    val d = invokeLinear(.0.toRad().toVector()).to2D().toAngle()
    return Pose2D(p, d)
}

fun Pose2D.toTransformation(): Transformation {
    val (x, y) = p
    val (cos, sin) = d.toVector()
    return Transformation(
        matrix {
            row(+cos, -sin, x)
            row(+sin, +cos, y)
            row(0, 0, 1)
        })
}

fun Transformation.toPose3D(): Pose3D {
    require(dim == 3) { "3d transformation is required" }
    val p = invoke(vector3DOfZero()).to3D()
    TODO()
}

fun Pose3D.toTransformation(): Transformation {
    val (x, y, z) = p
    val half = theta.asRadian() / 2
    val a = cos(half)
    val (b, c, d) = u * sin(half)

    val ab = a * b
    val ac = a * c
    val ad = a * d
    val bc = b * c
    val bd = b * d
    val cd = c * d

    val b2 = b * b
    val c2 = c * c
    val d2 = d * d

    return Transformation(
        matrix {
            row(1 - 2 * c2 - 2 * d2, 2 * bc - 2 * ad, 2 * ac + 2 * bd, x)
            row(2 * bc + 2 * ad, 1 - 2 * b2 - 2 * d2, 2 * cd - 2 * ab, y)
            row(2 * bd - 2 * ac, 2 * ab + 2 * cd, 1 - 2 * b2 - 2 * c2, z)
            row(0, 0, 0, 1)
        })
}

fun Pose2D.toPose3D(): Pose3D =
    Pose3D(p = vector3DOf(p.x, p.y, 0),
           d = vector3DOf(0, 0, d.asRadian()))
