package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.implement.vector.vector3DOf
import kotlin.math.sqrt

/** 四元数 */
data class Quaternion(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double
) {
    /** 实部 */
    val r get() = a

    /** 虚部 */
    val v get() = vector3DOf(b, c, d)

    /** 平方模长 */
    val square by lazy { a * a + b * b + c * c + d * d }

    /** 模长 */
    val length by lazy { sqrt(square) }

    /** 共轭 */
    val conjugate get() = Quaternion(a, -b, -c, -d)

    /** 求逆 */
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

    operator fun times(others: Quaternion): Quaternion {
        val (e, f, g, h) = others
        return Quaternion(
            a * e - b * f - c * g - d * h,
            b * e + a * f - d * g + c * h,
            c * e + d * f + a * g - b * h,
            d * e - c * f + b * g + a * h)
    }
}
