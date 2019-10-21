package org.mechdancer.geometry.rotation3d

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.function.vector.norm
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.algebra.implement.vector.vector3DOf
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.toRad
import org.mechdancer.geometry.rotation3d.AxesOrder.*
import kotlin.math.*

sealed class Angle3D(val first: Angle, val second: Angle, val third: Angle, val axesOrder: AxesOrder) {

    val matrix: Matrix by lazy {
        when (when (this) {
            is Euler        -> axesOrder
            is RollPitchYaw -> axesOrder.reverse()
        }) {
            ZYZ -> rz(third) * ry(second) * rz(first)
            ZXZ -> rz(third) * rx(second) * rz(first)
            YZY -> ry(third) * rz(second) * ry(first)
            YXY -> ry(third) * rx(second) * ry(third)
            XZX -> rx(third) * rz(second) * rx(first)
            XYX -> rx(third) * ry(second) * rx(first)
            XYZ -> rx(third) * ry(second) * rz(first)
            XZY -> rx(third) * rz(second) * ry(first)
            YXZ -> ry(third) * rx(second) * rz(first)
            YZX -> ry(third) * rz(second) * rx(first)
            ZXY -> rz(third) * rx(second) * ry(first)
            ZYX -> rz(third) * ry(second) * rx(first)
        }
    }

    fun toEuler() = when (this) {
        is Euler        -> this
        is RollPitchYaw -> Euler(first, second, third, axesOrder.reverse())
    }

    fun toRollPitchYaw() = when (this) {
        is RollPitchYaw -> this
        is Euler        -> RollPitchYaw(first, second, third, axesOrder.reverse())
    }


    companion object {
        fun rx(angle: Angle) = angle.asRadian().let { a ->
            matrix {
                row(1, 0, 0)
                row(0, cos(a), -sin(a))
                row(0, sin(a), cos(a))
            }
        }

        fun ry(angle: Angle) = angle.asRadian().let { a ->
            matrix {
                row(cos(a), 0, sin(a))
                row(0, 1, 0)
                row(-sin(a), 0, cos(a))
            }
        }

        fun rz(angle: Angle) = angle.asRadian().let { a ->
            matrix {
                row(cos(a), -sin(a), 0)
                row(sin(a), cos(a), 0)
                row(0, 0, 1)
            }
        }

        inline fun <reified T : Angle3D> fromMatrix(matrix: Matrix, axesOrder: AxesOrder): T {
            val pos = solve(matrix, axesOrder, false)
            val neg = solve(matrix, axesOrder, true)

            val result = listOf(
                vector3DOf(pos.first.asRadian(), pos.second.asRadian(), pos.third.asRadian()),
                vector3DOf(neg.first.asRadian(), neg.second.asRadian(), neg.third.asRadian())
            ).associateWith { it.norm() }.maxBy { it.value }!!.key
                .let { RollPitchYaw(it.x.toRad(), it.y.toRad(), it.z.toRad(), axesOrder) }
            return when (T::class.java) {
                Euler::class.java        -> result.toEuler() as T
                RollPitchYaw::class.java -> result as T
                else                     -> throw IllegalStateException()
            }
        }

        @PublishedApi
        internal fun solve(matrix: Matrix, axesOrder: AxesOrder, neg: Boolean): RollPitchYaw {

            val test: Double
            val firstAngle: Double
            val secondAngle: Double
            val thirdAngle: Double

            when (axesOrder) {
                XZX -> {
                    test = matrix[0, 0]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(2, 1) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[2, 1], matrix[1, 1]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 2) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(2, 2) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[1, 2], matrix[2, 2]))
                        }
                        else                     -> {
                            /*  matrix.get(0, 0) == cos(secondAngle)  */
                            secondAngle = (if (neg) acos(matrix[0, 0]) else -acos(matrix[0, 0]))
                            /*  matrix.get(0, 2) == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix.get(0, 1) == -(cos(firstAngle) * sin(secondAngle))  */
                            firstAngle = atan2(matrix[0, 2] / sin(secondAngle), -matrix[0, 1] / sin(secondAngle))
                            /*  matrix.get(2, 0) == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(1, 0) == cos(thirdAngle) * sin(secondAngle)  */
                            thirdAngle = atan2(matrix[2, 0] / sin(secondAngle), matrix[1, 0] / sin(secondAngle))
                        }
                    }
                }
                XYX -> {
                    test = matrix[0, 0]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(2, 1) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix.get(2, 1), matrix.get(1, 1)) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 2) == -sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(-matrix.get(1, 2), matrix.get(1, 1)))
                        }
                        else                     -> {
                            /*  matrix.get(0, 0) == cos(secondAngle)  */
                            secondAngle = (if (neg) acos(matrix.get(0, 0)) else -acos(matrix.get(0, 0)))
                            /*  matrix.get(0, 1) == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix.get(0, 2) == cos(firstAngle) * sin(secondAngle)  */
                            firstAngle = atan2(matrix.get(0, 1) / sin(secondAngle), matrix.get(0, 2) / sin(secondAngle))
                            /*  matrix.get(1, 0) == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(2, 0) == -(cos(thirdAngle) * sin(secondAngle))  */
                            thirdAngle = atan2(matrix.get(1, 0) / sin(secondAngle), -matrix.get(2, 0) / sin(secondAngle))
                        }
                    }
                }
                YXY -> {
                    test = matrix[1, 1]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 2], matrix[0, 0]))
                        }
                        else                     -> {
                            /*  matrix.get(1, 1) == cos(secondAngle)  */
                            secondAngle = (if (neg) acos(matrix[1, 1]) else -acos(matrix[1, 1]))
                            /*  matrix.get(1, 0) == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix.get(1, 2) == -(cos(firstAngle) * sin(secondAngle))  */
                            firstAngle = atan2(matrix[1, 0] / sin(secondAngle), -matrix[1, 2] / sin(secondAngle))
                            /*  matrix.get(0, 1) == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(2, 1) == cos(thirdAngle) * sin(secondAngle)  */
                            thirdAngle = atan2(matrix[0, 1] / sin(secondAngle), matrix[2, 1] / sin(secondAngle))
                        }
                    }
                }
                YZY -> {
                    test = matrix[1, 1]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == -sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(2, 2) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(-matrix[0, 2], matrix[2, 2]))
                        }
                        else                     -> {
                            /*  matrix.get(1, 1) == cos(secondAngle)  */
                            secondAngle = (if (neg) acos(matrix[1, 1]) else -acos(matrix[1, 1]))
                            /*  matrix.get(1, 2) == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix.get(1, 0) == cos(firstAngle) * sin(secondAngle)  */
                            firstAngle = atan2(matrix[1, 2] / sin(secondAngle), matrix[1, 0] / sin(secondAngle))
                            /*  matrix.get(2, 1) == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(0, 1) == -(cos(thirdAngle) * sin(secondAngle))  */
                            thirdAngle = atan2(matrix[2, 1] / sin(secondAngle), -matrix[0, 1] / sin(secondAngle))
                        }
                    }
                }
                ZYZ -> {
                    test = matrix[2, 2]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 0) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[1, 0], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 1) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 1], matrix[1, 1]))
                        }
                        else                     -> {
                            /*  matrix.get(2, 2) == cos(secondAngle)  */
                            secondAngle = (if (neg) acos(matrix[2, 2]) else -acos(matrix[2, 2]))
                            /*  matrix.get(2, 1) == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix.get(2, 0) == -(cos(firstAngle) * sin(secondAngle))  */
                            firstAngle = atan2(matrix[2, 1] / sin(secondAngle), -matrix[2, 0] / sin(secondAngle))
                            /*  matrix.get(1, 2) == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(0, 2) == cos(thirdAngle) * sin(secondAngle)  */
                            thirdAngle = atan2(matrix[1, 2] / sin(secondAngle), matrix[0, 2] / sin(secondAngle))
                        }
                    }
                }
                ZXZ -> {
                    test = matrix[2, 2]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 0) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[1, 0], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 1) == -sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(-matrix[0, 1], matrix[0, 0]))
                        }
                        else                     -> {
                            /*  matrix.get(2, 2) == cos(secondAngle)  */
                            secondAngle = (if (neg) acos(matrix[2, 2]) else -acos(matrix[2, 2]))
                            /*  matrix.get(2, 0) == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix.get(2, 1) == cos(firstAngle) * sin(secondAngle)  */
                            firstAngle = atan2(matrix[2, 0] / sin(secondAngle), matrix[2, 1] / sin(secondAngle))
                            /*  matrix.get(0, 2) == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(1, 2) == -(cos(thirdAngle) * sin(secondAngle))  */
                            thirdAngle = atan2(matrix[0, 2] / sin(secondAngle), -matrix[1, 2] / sin(secondAngle))
                        }
                    }
                }
                XZY -> {
                    test = matrix[1, 0]  /*  sin(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(2, 2) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[2, 2]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(2, 1) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(0, 1) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[2, 1], matrix[0, 1]))
                        }
                        else                     -> {
                            /*  matrix.get(1, 0) == sin(secondAngle)  */
                            secondAngle = (if (neg) asin(matrix[1, 0]) else PI - asin(matrix[1, 0]))
                            /*  matrix.get(1, 2) == -(cos(secondAngle) * sin(firstAngle))  */
                            /*  matrix.get(1, 1) == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(-matrix[1, 2] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                            /*  matrix.get(2, 0) == -(cos(secondAngle) * sin(thirdAngle))  */
                            /*  matrix.get(0, 0) == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(-matrix[2, 0] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                        }
                    }
                }
                XYZ -> {
                    test = matrix[2, 0]  /*  -sin(secondAngle)  */
                    when {
                        doubleEquals(test, -1.0) -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 1) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(0, 2) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 1], matrix[0, 2]))
                        }
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 1) == -sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(-matrix[0, 1], matrix[1, 1]) - firstAngle)
                        }
                        else                     -> {
                            /*  matrix.get(2, 0) == -sin(secondAngle)  */
                            secondAngle = (if (neg) -asin(matrix[2, 0]) else PI + asin(matrix[2, 0]))
                            /*  matrix.get(2, 1) == cos(secondAngle) * sin(firstAngle)  */
                            /*  matrix.get(2, 2) == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(matrix[2, 1] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                            /*  matrix.get(1, 0) == cos(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(matrix[1, 0] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                        }
                    }
                }
                YXZ -> {
                    test = matrix[2, 1]  /*  sin(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 2) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 2], matrix[0, 0]))
                        }
                        else                     -> {
                            /*  matrix.get(2, 1) == sin(secondAngle)  */
                            secondAngle = (if (neg) asin(matrix[2, 1]) else PI - asin(matrix[2, 1]))
                            /*  matrix.get(2, 0) == -(cos(secondAngle) * sin(firstAngle))  */
                            /*  matrix.get(2, 2) == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(-matrix[2, 0] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                            /*  matrix.get(0, 1) == -(cos(secondAngle) * sin(thirdAngle))  */
                            /*  matrix.get(1, 1) == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(-matrix[0, 1] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                        }
                    }
                }
                YZX -> {
                    test = matrix[0, 1]  /*  -sin(secondAngle)  */
                    when {
                        doubleEquals(test, -1.0) -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 2) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(1, 0) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[1, 2], matrix[1, 0]))
                        }
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 2) == -sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(2, 2) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(-matrix[1, 2], matrix[2, 2]) - firstAngle)
                        }
                        else                     -> {
                            /*  matrix.get(0, 1) == -sin(secondAngle)  */
                            secondAngle = (if (neg) -asin(matrix[0, 1]) else PI + asin(matrix[0, 1]))
                            /*  matrix.get(0, 2) == cos(secondAngle) * sin(firstAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(matrix[0, 2] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                            /*  matrix.get(2, 1) == cos(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(matrix[2, 1] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                        }
                    }
                }
                ZYX -> {
                    test = matrix[0, 2]  /*  sin(secondAngle)  */
                    when {
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 0) == sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[1, 0], matrix[1, 1]) - firstAngle)
                        }
                        doubleEquals(test, -1.0) -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(1, 0) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[1, 0], matrix[1, 1]))
                        }
                        else                     -> {
                            /*  matrix.get(0, 2) == sin(secondAngle)  */
                            secondAngle = (if (neg) asin(matrix[0, 2]) else PI - asin(matrix[0, 2]))
                            /*  matrix.get(0, 1) == -(cos(secondAngle) * sin(firstAngle))  */
                            /*  matrix.get(0, 0) == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(-matrix[0, 1] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                            /*  matrix.get(1, 2) == -(cos(secondAngle) * sin(thirdAngle))  */
                            /*  matrix.get(2, 2) == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(-matrix[1, 2] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                        }
                    }
                }
                ZXY -> {
                    test = matrix[1, 2]  /*  -sin(secondAngle)  */
                    when {
                        doubleEquals(test, -1.0) -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(2, 0) == sin(firstAngle - thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[2, 0], matrix[0, 0]))
                        }
                        doubleEquals(test, 1.0)  -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix.get(0, 1) == -sin(firstAngle + thirdAngle)  */
                            /*  matrix.get(0, 0) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(-matrix[0, 1], matrix[0, 0]) - firstAngle)
                        }
                        else                     -> {
                            /*  matrix.get(1, 2) == -sin(secondAngle)  */
                            secondAngle = (if (neg) -asin(matrix[1, 2]) else PI + asin(matrix[1, 2]))
                            /*  matrix.get(1, 0) == cos(secondAngle) * sin(firstAngle)  */
                            /*  matrix.get(1, 1) == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(matrix[1, 0] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                            /*  matrix.get(0, 2) == cos(secondAngle) * sin(thirdAngle)  */
                            /*  matrix.get(2, 2) == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(matrix[0, 2] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                        }
                    }
                }
            }
            return RollPitchYaw(
                firstAngle.toRad(),
                secondAngle.toRad(),
                thirdAngle.toRad(),
                axesOrder)
        }
    }


    // Intrinsic
    class Euler(first: Angle, second: Angle, third: Angle, axesOrder: AxesOrder) : Angle3D(first, second, third, axesOrder)

    // Extrinsic
    class RollPitchYaw(first: Angle, second: Angle, third: Angle, axesOrder: AxesOrder) : Angle3D(first, second, third, axesOrder)

}