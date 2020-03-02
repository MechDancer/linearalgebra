package org.mechdancer.algebra.function.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.implement.vector.*
import org.mechdancer.algebra.uniqueValue
import org.mechdancer.algebra.zipFast
import kotlin.math.abs
import kotlin.math.pow

operator fun Vector.times(k: Number) = toList().map { it * k.toDouble() }.let(::ListVector)
operator fun Vector.div(k: Number) = toList().map { it / k.toDouble() }.let(::ListVector)

operator fun Vector2D.times(k: Number) = k.toDouble().let { Vector2D(x * it, y * it) }
operator fun Vector2D.div(k: Number) = k.toDouble().let { Vector2D(x / it, y / it) }

operator fun Vector3D.times(k: Number) = k.toDouble().let { Vector3D(x * it, y * it, z * it) }
operator fun Vector3D.div(k: Number) = k.toDouble().let { Vector3D(x / it, y / it, z / it) }

private fun differentDimException(a: Vector, b: Vector) =
    IllegalArgumentException("operate two vector of different dim (${a.dim} and ${b.dim})")

private inline fun Vector.zip(other: Vector, block: (Double, Double) -> Double) =
    takeIf { dim == other.dim }
        ?.let { toList().zipFast(other.toList(), block) }
    ?: throw differentDimException(this, other)

private inline fun Vector.zipToNew(other: Vector, block: (Double, Double) -> Double) =
    zip(other, block).let(::ListVector)

operator fun Vector.plus(other: Vector) = zipToNew(other) { a, b -> a + b }
operator fun Vector.minus(other: Vector) = zipToNew(other) { a, b -> a - b }
operator fun Vector.times(other: Vector) = zipToNew(other) { a, b -> a * b }
operator fun Vector.div(other: Vector) = zipToNew(other) { a, b -> a / b }
infix fun Vector.dot(other: Vector) = zip(other) { a, b -> a * b }.sum()

operator fun Vector2D.plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
operator fun Vector2D.minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
operator fun Vector2D.times(other: Vector2D) = Vector2D(x * other.x, y * other.y)
operator fun Vector2D.div(other: Vector2D) = Vector2D(x / other.x, y / other.y)
infix fun Vector2D.dot(other: Vector2D) = x * other.x + y * other.y

operator fun Vector3D.plus(other: Vector3D) = Vector3D(x + other.x, y + other.y, z + other.z)
operator fun Vector3D.minus(other: Vector3D) = Vector3D(x - other.x, y - other.y, z - other.z)
operator fun Vector3D.times(other: Vector3D) = Vector3D(x * other.x, y * other.y, z * other.z)
operator fun Vector3D.div(other: Vector3D) = Vector3D(x / other.x, y / other.y, z / other.z)
infix fun Vector3D.dot(other: Vector3D) = x * other.x + y * other.y + z * other.z
infix fun Vector3D.cross(other: Vector3D) = Vector3D(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
)

operator fun Vector.unaryMinus() = toList().map { -it }.let(::ListVector)
operator fun Vector2D.unaryMinus() = Vector2D(-x, -y)
operator fun Vector3D.unaryMinus() = Vector3D(-x, -y, -z)

fun Vector.normalize() =
    if (doubleEquals(length, .0))
        listVectorOfZero(dim)
    else
        div(length)

fun Vector2D.normalize() =
    if (doubleEquals(length, .0))
        vector2DOfZero()
    else
        div(length)

fun Vector3D.normalize() =
    if (doubleEquals(length, .0))
        vector3DOfZero()
    else
        div(length)

fun Sequence<Vector>.sum() =
    uniqueValue(Vector::dim)
        ?.let(::listVectorOfZero)
        ?.let { fold(it) { sum, v -> sum + v } }
    ?: throw UnsupportedOperationException("vector dimensions are different")

fun Sequence<Vector2D>.sum() =
    fold(vector2DOfZero()) { sum, v -> sum + v }

fun Sequence<Vector3D>.sum() =
    fold(vector3DOfZero()) { sum, v -> sum + v }

fun Iterable<Vector>.sum() = asSequence().sum()
fun Iterable<Vector2D>.sum() = asSequence().sum()
fun Iterable<Vector3D>.sum() = asSequence().sum()

/** [n]范数 */
fun Vector.norm(n: Int = 2) =
    when (n) {
        -1, Int.MAX_VALUE -> toList().map(::abs).max()
        1                 -> toList().sumByDouble(::abs)
        2                 -> length
        else              -> toList().sumByDouble { it.pow(n) }.pow(1.0 / n)
    } ?: .0

/** 施密特正交化 */
fun Sequence<Vector>.orthogonalize(): List<Vector> =
    fold(mutableListOf<Vector>()) { result, v0 ->
        result += sequence { yield(v0); for (v in result) yield(v * -(v0 dot v)) }.sum().normalize()
        result
    }

/** 施密特正交化 */
fun Iterable<Vector>.orthogonalize() =
    asSequence().orthogonalize()

/** 施密特正交化 */
fun Array<Vector>.orthogonalize() =
    asSequence().orthogonalize()
