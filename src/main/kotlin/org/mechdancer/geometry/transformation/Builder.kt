package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.algebra.implement.vector.vector2D
import org.mechdancer.algebra.implement.vector.vector3D
import org.mechdancer.algebra.implement.vector.vector3DOfZero
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.toRad
import org.mechdancer.geometry.angle.toVector
import kotlin.math.atan2

fun pose2D(x: Number = 0, y: Number = 0, theta: Angle) =
    Pose2D(vector2D(x, y), theta)

fun pose2D(x: Number = 0, y: Number = 0, theta: Number = 0) =
    Pose2D(vector2D(x, y), theta.toRad())

fun pose3D(x: Number = 0, y: Number = 0, z: Number = 0, d: Vector3D) =
    Pose3D(vector3D(x, y, z), d)

fun pose3D(x: Number = 0, y: Number = 0, z: Number = 0,
           dx: Number = 0, dy: Number = 0, dz: Number = 0) =
    Pose3D(vector3D(x, y, z), vector3D(dx, dy, dz))

fun quaternion(a: Number = 0, b: Number = 0, c: Number = 0, d: Number = 0) =
    Quaternion(a.toDouble(), b.toDouble(), c.toDouble(), d.toDouble())

fun quaternion(r: Number = 0, v: Vector3D = vector3DOfZero()) =
    Quaternion(r.toDouble(), v.x, v.y, v.z)

fun MatrixTransformation.toPose2D(): Pose2D {
    require(dim == 2) { "2d transformation is required" }
    return Pose2D(vector2D(matrix[0, 2], matrix[1, 2]),
                  atan2(matrix[0, 0], matrix[1, 0]).toRad())
}

fun Pose2D.toTransformation(): MatrixTransformation {
    val (x, y) = p
    val (cos, sin) = d.toVector()
    return MatrixTransformation(
        matrix {
            row(+cos, -sin, x)
            row(+sin, +cos, y)
            row(0, 0, 1)
        })
}

fun Pose2D.to3D(): Pose3D =
    Pose3D(p = vector3D(p.x, p.y, 0),
           d = vector3D(0, 0, d.asRadian() / 2))
