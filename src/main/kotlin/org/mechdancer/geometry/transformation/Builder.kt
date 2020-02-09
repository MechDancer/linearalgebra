package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.function.vector.normalize
import org.mechdancer.algebra.function.vector.times
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.Vector3D
import org.mechdancer.algebra.implement.vector.vector2D
import org.mechdancer.algebra.implement.vector.vector3D
import org.mechdancer.algebra.implement.vector.vector3DOfZero
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.adjustRight
import org.mechdancer.geometry.angle.toRad
import org.mechdancer.geometry.angle.toVector
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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

fun Pose2D.to3D(): Pose3D =
    Pose3D(p = vector3D(p.x, p.y, 0),
           d = vector3D(0, 0, d.rad / 2))

fun MatrixTransformation.toPose2D(): Pose2D {
    require(dim == 2) { "2d transformation is required" }
    return Pose2D(vector2D(matrix[0, 2], matrix[1, 2]),
                  atan2(matrix[1, 0], matrix[0, 0]).toRad())
}

fun Pose2D.toMatrixTransformation(): MatrixTransformation {
    val (x, y) = p
    val (cos, sin) = d.toVector()
    return MatrixTransformation(
        matrix {
            row(+cos, -sin, x)
            row(+sin, +cos, y)
            row(0, 0, 1)
        })
}

fun Quaternion.toMatrixL() =
    matrix {
        row(+a, -b, -c, -d)
        row(+b, +a, -d, +c)
        row(+c, +d, +a, -b)
        row(+d, -c, +b, +a)
    }

fun Quaternion.toMatrixR() =
    matrix {
        row(+a, -b, -c, -d)
        row(+b, +a, +d, -c)
        row(+c, -d, +a, +b)
        row(+d, +c, -b, +a)
    }

fun Pose3D.toMatrixTransformation(): MatrixTransformation {
    val half = d.length.adjustRight(PI, PI)
    val a = cos(half)
    val (b, c, d) = d.normalize() * sin(half)
    val (x, y, z) = p
    return MatrixTransformation(
        matrix {
            row(1 - 2 * c * c - 2 * d * d, 2 * b * c - 2 * a * d, 2 * a * c + 2 * b * d, x)
            row(2 * b * c + 2 * a * d, 1 - 2 * b * b - 2 * d * d, 2 * c * d - 2 * a * b, y)
            row(2 * b * d - 2 * a * c, 2 * a * b + 2 * c * d, 1 - 2 * b * b - 2 * c * c, z)
            row(0, 0, 0, 1)
        }
    )
}
