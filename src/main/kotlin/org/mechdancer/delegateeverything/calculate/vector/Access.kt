package org.mechdancer.delegateeverything.calculate.vector

import org.mechdancer.delegateeverything.core.Vector

//解绑定

operator fun Vector.component1() = this[0]
operator fun Vector.component2() = this[1]
operator fun Vector.component3() = this[2]
operator fun Vector.component4() = this[3]
operator fun Vector.component5() = this[4]

// 其中各维度的名字

val Vector.x get() = this[0]
val Vector.y get() = this[1]
val Vector.z get() = this[2]
