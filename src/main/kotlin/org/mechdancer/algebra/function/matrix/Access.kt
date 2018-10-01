package org.mechdancer.algebra.function.matrix

import org.mechdancer.algebra.core.Matrix

val Matrix.firstRow get() = row(0)
val Matrix.lastRow get() = row(row - 1)

val Matrix.firstColumn get() = column(0)
val Matrix.lastColumn get() = column(column - 1)
