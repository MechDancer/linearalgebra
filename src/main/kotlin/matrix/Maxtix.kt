package matrix

import vector.Vector

interface Maxtix {

	val dimension: Int

	val defineType: DefineType

	val data: List<List<Double>>

	val row: Int

	val column: Int

	operator fun get(row: Int, column: Int): Double

	operator fun plus(other: Maxtix): Maxtix

	operator fun minus(other: Maxtix): Maxtix

	operator fun times(k: Double): Maxtix

	operator fun times(other: Maxtix): Maxtix

	operator fun times(vector: Vector): Vector

	operator fun div(other: Maxtix): Maxtix


	fun companion(): Maxtix

	fun transpose(): Maxtix

	fun inverse(): Maxtix


}