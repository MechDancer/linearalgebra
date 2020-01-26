package org.mechdancer.geometry.transformation

import org.mechdancer.algebra.core.Vector

/**
 * 坐标变换
 *
 * `A` 坐标系上的 `B` 变换，
 * 将 `B` 坐标系上的坐标变换到 `A` 坐标系上，
 * 记作 `B` -> `A`
 */
interface Transformation<T : Transformation<T>> {
    /**
     * 维数
     *
     * 变换只能处理同维的坐标或变换
     */
    val dim: Int

    /**
     * 变换
     *
     * @param p p on `B`
     * @return p' on `A`
     */
    operator fun times(p: Vector): Vector

    /**
     * 复合
     *
     * - `this` := `B` -> `A`
     * - [tf] := `C` -> `B`
     * @return `C` -> `A`
     */
    operator fun times(tf: T): T

    /**
     * 求逆
     *
     * @return `A` -> `B`
     */
    fun inverse(): T
}
