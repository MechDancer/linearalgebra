package org.mechdancer.algebra.core

abstract class Determinant : ValueMutableMatrix {
	@Volatile
	private var rankAvailable = false

	@Volatile
	private var detAvailable = false

	protected fun disable() {
		rankAvailable = false
		detAvailable = false
	}

	protected abstract fun updateRank(): Int
	protected abstract fun updateDet(): Double

	override fun timesRow(r: Int, k: Double) {
		if (detAvailable) det *= k
	}

	override fun exchangeRow(r0: Int, r1: Int) {
		if (detAvailable) det *= -1
	}

	override fun timesColumn(c: Int, k: Double) {
		if (detAvailable) det *= k
	}

	override fun exchangeColumn(c0: Int, c1: Int) {
		if (detAvailable) det *= -1
	}

	final override var rank = 0
		get() {
			if (!rankAvailable)
				field = updateRank()
			rankAvailable = true
			return field
		}
		private set

	final override var det = .0
		get() {
			if (!detAvailable)
				field = updateDet()
			detAvailable = true
			return field
		}
		private set
}
