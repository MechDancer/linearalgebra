package org.mechdancer.algebra.implement.vector

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.function.vector.x
import org.mechdancer.algebra.function.vector.y
import org.mechdancer.algebra.function.vector.z

// vector -> vector

fun Vector.to2D(): Vector2D =
    this as? Vector2D
    ?: takeIf { dim == 2 }?.let { Vector2D(x, y) }
    ?: throw IllegalArgumentException()

fun Vector.to3D(): Vector3D =
    this as? Vector3D
    ?: takeIf { dim == 3 }?.let { Vector3D(x, y, z) }
    ?: throw IllegalArgumentException()

fun Vector.toListVector(): ListVector =
    this as? ListVector ?: ListVector(toList())

// iterable -> vector

fun Iterable<Number>.toListVector() =
    map { it.toDouble() }.let(::ListVector)

// array -> vector

fun Array<Number>.toListVector() =
    map { it.toDouble() }.let(::ListVector)

fun DoubleArray.toListVector() =
    toList().let(::ListVector)

// dsl builder

fun listVectorOf(vararg item: Number) =
    ListVector(item.map { it.toDouble() })

fun listVectorOfZero(dim: Int) =
    ListVector(List(dim) { .0 })

fun vector2D(x: Number, y: Number) =
    Vector2D(x.toDouble(), y.toDouble())

fun vector2DOfZero() =
    Vector2D(.0, .0)

fun vector3D(x: Number, y: Number, z: Number) =
    Vector3D(x.toDouble(), y.toDouble(), z.toDouble())

fun vector3DOfZero() = vector3D(0, 0, 0)

// to sub-vector

infix fun Vector.without(i: Int) =
    VectorExcludeOne(this, i)
