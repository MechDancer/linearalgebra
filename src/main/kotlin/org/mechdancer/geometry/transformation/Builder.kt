package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.algebra.implement.vector.vector2DOf
import org.mechdancer.algebra.implement.vector.vector3DOf
import org.mechdancer.algebra.implement.vector.vector3DOfZero
import org.mechdancer.geometry.angle.toRad
import org.mechdancer.geometry.angle.toVector
import kotlin.math.atan2

fun pose2D(x: Number = 0, y: Number = 0, theta: Number = 0) =
    Pose2D(vector2DOf(x, y), theta.toRad())

fun odometry(x: Number = 0, y: Number = 0, theta: Number = 0) =
    Pose2D(vector2DOf(x, y), theta.toRad())

fun pose3D(x: Number = 0, y: Number = 0, z: Number = 0, d: Vector3D = vector3DOfZero()) =
    Pose3D(vector3DOf(x, y, z), d)

fun quaternion(a: Number = 0, b: Number = 0, c: Number = 0, d: Number = 0) =
    Quaternion(a.toDouble(), b.toDouble(), c.toDouble(), d.toDouble())

fun quaternion(r: Number = 0, v: Vector3D = vector3DOfZero()) =
    Quaternion(r.toDouble(), v.x, v.y, v.z)

fun Transformation.toPose2D(): Pose2D {
    require(dim == 2) { "2d transformation is required" }
    return Pose2D(vector2DOf(matrix[0, 2], matrix[1, 2]),
                  atan2(matrix[0, 0], matrix[1, 0]).toRad())
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

fun Pose2D.to3D(): Pose3D =
    Pose3D(p = vector3DOf(p.x, p.y, 0),
           d = vector3DOf(0, 0, d.asRadian() / 2))
