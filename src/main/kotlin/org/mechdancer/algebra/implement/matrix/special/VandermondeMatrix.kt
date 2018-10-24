package org.mechdancer.algebra.implement.matrix.special

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.function.matrix.determinantValue
import org.mechdancer.algebra.implement.matrix.builder.listMatrixOf
import kotlin.math.pow

/**
 * 范德蒙行列式
 */
class VandermondeMatrix
private constructor(
	elements: List<Double>
) : Matrix by listMatrixOf(
	elements.size,
	elements.size,
	{ r, c -> elements[c].pow(r) }
) {
	override val det: Double =
		if (elements.size >= 2) {
			elements.indices.reversed().fold(1.0) { acc, i ->
				acc * (i - 1 downTo 0).fold(1.0) { sum, j ->
					sum * (elements[i] - elements.getOrElse(j) { .0 }.toDouble())
				}
			}
		} else determinantValue()

	companion object {
		operator fun get(elements: Iterable<Number>) =
			VandermondeMatrix(elements.map(Number::toDouble))
	}
}
