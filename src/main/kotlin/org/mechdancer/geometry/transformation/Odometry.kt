package org.mechdancer.geometry.transformation

interface Odometry<T : Odometry<T>> {
    /** 增量 [delta] 累加到里程 */
    infix fun plusDelta(delta: T): T

    /** 里程回滚到增量 [delta] 之前 */
    infix fun minusDelta(delta: T): T

    /** 计算里程从标记 [mark] 到当前状态的增量 */
    infix fun minusState(mark: T): T
}
