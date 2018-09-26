package org.mechdancer.algebra

fun dimensionArgumentError(): Nothing =
		throw IllegalArgumentException("dimension error")

fun dimensionStateError(): Nothing =
		throw IllegalStateException("dimension error")
