import kotlin.math.sqrt

open class VectorImpl(override val data: List<Double>) : Vector {

	override val dimension: Int
		get() = data.size


	private fun operate(other: Vector, operation: (v1: Double, v2: Double) -> Double): Vector {
		checkDimension(other)
		return data.indices.map { operation(data[it], other.data[it]) }.let(::VectorImpl)
	}


	private fun checkDimension(other: Vector) =
			if (this.dimension != other.dimension) throw IllegalArgumentException("维度不同,无法操作")
			else Unit

	override operator fun get(index: Int): Double = data[index]

	override operator fun plus(other: Vector): Vector = operate(other) { v1, v2 -> v1 + v2 }

	override operator fun minus(other: Vector): Vector = operate(other) { v1, v2 -> v1 - v2 }

	override operator fun div(k: Double): Vector = VectorImpl(data.map { it / k })

	override operator fun times(k: Double): Vector = VectorImpl(data.map { it * k })

	override fun times(other: Vector): Double {
		checkDimension(other)
		return (0 until dimension).sumByDouble { data[it] * other[it] }
	}

	override operator fun unaryMinus(): Vector = VectorImpl(data.map { -it })

	override infix fun to(other: Vector): Vector = other - this

	override fun norm(): Double = sqrt(data.sumByDouble { it * it })

	override fun toString(): String = "${dimension}DVector(${data.joinToString(separator = ",")})"

	override fun equals(other: Any?): Boolean {
		if (other !is Vector) return false
		return other.data == data
	}

	override fun hashCode(): Int {
		return data.hashCode()
	}
}


interface Vector {
	val dimension: Int

	val data: List<Double>

	operator fun get(index: Int): Double

	operator fun plus(other: Vector): Vector

	operator fun minus(other: Vector): Vector

	operator fun div(k: Double): Vector

	operator fun times(k: Double): Vector

	operator fun times(other: Vector): Double

	operator fun unaryMinus(): Vector

	infix fun to(other: Vector): Vector

	fun norm(): Double

	companion object {

		fun zeroOf(dimension: Int): Vector {
			if (dimension <= 0) throw IllegalArgumentException("维度参数错误")
			return DoubleArray(dimension).toList().let(::VectorImpl)
		}

		fun normalize(vector: Vector): Vector = vector / vector.norm()

	}

}

enum class Axis3D { X, Y, Z }

class Vector2D(x: Double, y: Double) : VectorImpl(listOf(x, y)) {
	override val dimension: Int = 2

	operator fun get(axis3D: Axis3D) = when (axis3D) {
		Axis3D.X -> this[0]
		Axis3D.Y -> this[1]
		else     -> throw IllegalArgumentException("维度参数错误")
	}
}

class Vector3D(x: Double, y: Double, z: Double) : VectorImpl(listOf(x, y, z)) {
	override val dimension: Int = 3

	operator fun get(axis3D: Axis3D) = when (axis3D) {
		Axis3D.X -> this[0]
		Axis3D.Y -> this[1]
		Axis3D.Z -> this[2]
	}

	infix fun x(other: Vector3D): Vector {
		return listOf(
				this[1] * other[2] - this[2] * other[1],
				this[2] * other[0] - this[0] * other[2],
				this[0] * other[1] - this[1] * other[0]).let(::VectorImpl)
	}
}

