package org.mechdancer.algebra.core

/**
 * Determinant
 * 行列式
 *
 * Using the properties of determinant elementary transformation
 *     to improve the performance of determinant computation
 *     in value mutable matrix
 * 利用行列式初等变换的性质优化值可变矩阵的行列式值计算性能
 */
abstract class Determinant : ValueMutableMatrix {
	@Volatile
	//直接赋值将破坏秩不变性
	private var rankAvailable = false

	@Volatile
	//直接赋值将使行列式值发生无法预期的变化
	private var detAvailable = false

	/**
	 * Changing values directly will invalid the determinant value
	 *     Then the value must be calculated again
	 *     Invoke this method to mark the value invalid
	 * 直接赋值操作将导致秩和行列式值无效化，需要重新计算
	 * 进行此类操作需调用此方法进行标记
	 */
	protected fun disable() {
		rankAvailable = false
		detAvailable = false
	}

	/**
	 * Invoke this method to update the rank
	 * 秩通过此方法进行更新，需要子类实现
	 */
	protected abstract fun updateRank(): Int

	/**
	 * Invoke this method to update the determinant value
	 * 行列式值通过此方法进行更新，需要子类实现
	 */
	protected abstract fun updateDet(): Double?

	override fun timesRow(r: Int, k: Double) {
		if (detAvailable) det = k * det!!
	}

	override fun exchangeRow(r0: Int, r1: Int) {
		if (detAvailable) det = -det!!
	}

	override fun timesColumn(c: Int, k: Double) {
		if (detAvailable) det = k * det!!
	}

	override fun exchangeColumn(c0: Int, c1: Int) {
		if (detAvailable) det = -det!!
	}

	final override var rank = 0
		get() {
			if (!rankAvailable) {
				field = updateRank()
				rankAvailable = true
			}
			return field
		}
		private set

	final override var det = .0.takeIf { row == column }
		get() {
			if (row == column && !detAvailable) {
				field = updateDet()
				detAvailable = true
			}
			return field
		}
		private set
}
