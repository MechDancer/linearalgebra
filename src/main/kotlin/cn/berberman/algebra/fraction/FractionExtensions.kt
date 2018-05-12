package cn.berberman.algebra.fraction


val fractionReg = Regex("\\s*(-?\\d+)\\s*/\\s*(-?\\d+)\\s*")

fun Int.toFraction(): Fraction = Fraction(this, 1)

fun String.toFractionOrNull(): Fraction? = fractionReg.find(this)?.groupValues?.let { r ->
	r.getOrNull(1)?.toIntOrNull()?.let { n ->
		r.getOrNull(2)?.toIntOrNull()?.let { d -> Fraction(n, d) }
	}
}

fun String.toFraction(): Fraction = fractionReg.find(this)!!.groupValues.let { r ->
	r[1].toInt().let { n ->
		r[2].toInt().let { d -> Fraction(n, d) }
	}
}

operator fun Int.plus(a: Fraction) = a + this

operator fun Int.minus(a: Fraction) = (a - this) * (-1)

operator fun Int.times(a: Fraction) = a * this

operator fun Int.div(a: Fraction) = a.reciprocal() * this

fun Double.toFraction(): Fraction = TODO()
