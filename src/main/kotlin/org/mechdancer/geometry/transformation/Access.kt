package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Vector
import org.mechdancer.algebra.implement.vector.Vector2D

/**
 * 点对集
 */
typealias PointMap = Map<out Vector, Vector>

/**
 * 二维点对集
 */
typealias Point2DMap = Map<Vector2D, Vector2D>

/**
 * 点对
 */
typealias PointPair = Pair<Vector, Vector>

/**
 * 二维点对
 */
typealias Point2DPair = Pair<Vector2D, Vector2D>

/**
 * 定义目标点在前
 */
val PointPair.target get() = first

/**
 * 定义出发点在后
 */
val PointPair.origin get() = second
