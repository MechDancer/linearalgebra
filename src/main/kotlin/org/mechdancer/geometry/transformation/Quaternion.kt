package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.listVectorOf
import org.mechdancer.algebra.implement.vector.vector3DOf
import kotlin.math.sqrt

/** 四元数 */
data class Quaternion(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double
) {
    val r get() = a
    val v get() = vector3DOf(b, c, d)

    val square by lazy { a * a + b * b + c * c + d * d }
    val length by lazy { sqrt(square) }

    val conjugate get() = Quaternion(a, -b, -c, -d)
    val inverse get() = conjugate / square

    operator fun plus(others: Quaternion) =
        Quaternion(a + others.a,
                   b + others.b,
                   c + others.c,
                   d + others.d)

    operator fun minus(others: Quaternion) =
        Quaternion(a - others.a,
                   b - others.b,
                   c - others.c,
                   d - others.d)

    operator fun times(k: Double) =
        Quaternion(a * k, b * k, c * k, d * k)

    operator fun div(k: Double) =
        Quaternion(a / k, b / k, c / k, d / k)

    operator fun times(others: Quaternion) =
        (matrix {
            row(+a, -b, -c, -d)
            row(+b, +a, -d, +c)
            row(+c, +d, +a, -b)
            row(+d, -c, +b, +a)
        } * listVectorOf(others.a, others.b, others.c, others.d)
        ).let { Quaternion(it[0], it[1], it[2], it[3]) }
}
