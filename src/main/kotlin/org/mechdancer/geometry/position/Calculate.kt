package org.mechdancer.geometry.position

import org.mechdancer.algebra.function.equation.solve
import org.mechdancer.algebra.function.vector.component1
import org.mechdancer.algebra.function.vector.component2
import org.mechdancer.algebra.function.vector.minus
import org.mechdancer.algebra.function.vector.plus
import org.mechdancer.algebra.implement.equation.builder.equations
import org.mechdancer.algebra.implement.matrix.builder.foldToRows
import org.mechdancer.algebra.implement.vector.Vector2D
import org.mechdancer.algebra.implement.vector.toListVector
import org.mechdancer.geometry.angle.Degree
import org.mechdancer.geometry.angle.Radian
import org.mechdancer.geometry.angle.rotate

fun Vector2D.rotate(angle: Number, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

fun Vector2D.rotate(angle: Degree, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

fun Vector2D.rotate(angle: Radian, centre: Vector2D) =
	minus(centre).rotate(angle).plus(centre)

/**
 * 最小二乘法从点对集推算空间变换子
 */
fun Map<Vector2D, Vector2D>.toTransformation() =
	takeIf { it.size >= 3 }
		?.flatMap {
			// -----------------------------------
			// ┌ x'┐ ┌ a b ┐┌ x ┐ ┌ e ┐
			// │   │=│     ││   │+│   │
			// └ y'┘ └ c d ┘└ y ┘ └ f ┘
			// -----------------------------------
			//   x' = ax + by + __ + __ +  e + __
			//   y' = __ + __ + cx + dy + __ +  f
			// -----------------------------------
			// ┌ x'┐ ┌ x y 0 0 1 0 ┐┌           ┐T
			// │   │=│             ││a b c d e f│
			// └ y'┘ └ 0 0 x y 0 1 ┘└           ┘
			// -----------------------------------
			val (absPose, relPose) = it
			val (x, y) = relPose
			equations {
				this[x, y, 0, 0, 1, 0] = absPose.x
				this[0, 0, x, y, 0, 1] = absPose.y
			}
		}
		// 方程列表整理为方程组
		?.toSet()
		// 解算
		?.solve()
		// 用列表表示
		?.toList()
		// 转换到仿射变换子
		?.let {
			Transformation(
				it.take(4) foldToRows 2,
				it.drop(4).toListVector()
			)
		}
