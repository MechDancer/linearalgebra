package vector

import matrix.DefineType
import matrix.Matrix
import matrix.impl.MatrixImpl


fun Vector.toMatrix(): Matrix =
		MatrixImpl(DefineType.COLUMN, listOf(data))
