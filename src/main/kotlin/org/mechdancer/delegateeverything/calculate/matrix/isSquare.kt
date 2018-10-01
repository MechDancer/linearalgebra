package org.mechdancer.delegateeverything.calculate.matrix

import org.mechdancer.delegateeverything.core.Matrix

fun Matrix.isSquare() = row == column
fun Matrix.isNotSquare() = row != column
