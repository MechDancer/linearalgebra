package org.mechdancer.algebra

internal fun dimensionArgumentError(): Nothing =
		throw IllegalArgumentException("dimension error")

internal fun dimensionStateError(): Nothing =
		throw IllegalStateException("dimension error")

internal fun rowNumberError(): Nothing =
		throw IllegalArgumentException("wrong row number")

internal fun columnNumberError(): Nothing =
		throw IllegalArgumentException("wrong column number")