package org.mechdancer.geometry.rotation3d

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.doubleEquals
import org.mechdancer.algebra.function.matrix.times
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.geometry.angle.Angle
import org.mechdancer.geometry.angle.toRad
import org.mechdancer.geometry.rotation3d.AxesOrder.*
import kotlin.math.*

/**
 * 三维旋转角（外旋）
 *
 * Extrinsic
 */
data class Angle3D(val first: Angle, val second: Angle, val third: Angle, val axesOrder: AxesOrder) {

    /**
     * 表示为旋转矩阵
     */
    val matrix: Matrix by lazy {
        when (axesOrder) {
            ZYZ -> rz(third) * ry(second) * rz(first)
            ZXZ -> rz(third) * rx(second) * rz(first)
            YZY -> ry(third) * rz(second) * ry(first)
            YXY -> ry(third) * rx(second) * ry(third)
            XZX -> rx(third) * rz(second) * rx(first)
            XYX -> rx(third) * ry(second) * rx(first)
            XYZ -> rz(third) * ry(second) * rx(first)
            XZY -> ry(third) * rz(second) * rx(first)
            YXZ -> rz(third) * rx(second) * ry(first)
            YZX -> rx(third) * rz(second) * ry(first)
            ZXY -> ry(third) * rx(second) * rz(first)
            ZYX -> rx(third) * ry(second) * rz(first)
        }
    }

    companion object {
        /**
         * 绕 X 轴逆时针旋转 [angle]
         */
        fun rx(angle: Angle) = angle.asRadian().let { a ->
            matrix {
                row(1, 0, 0)
                row(0, cos(a), -sin(a))
                row(0, sin(a), cos(a))
            }
        }

        /**
         * 绕 Y 轴逆时针旋转 [angle]
         */
        fun ry(angle: Angle) = angle.asRadian().let { a ->
            matrix {
                row(cos(a), 0, sin(a))
                row(0, 1, 0)
                row(-sin(a), 0, cos(a))
            }
        }

        /**
         * 绕 Z 轴逆时针旋转 [angle]
         */
        fun rz(angle: Angle) = angle.asRadian().let { a ->
            matrix {
                row(cos(a), -sin(a), 0)
                row(sin(a), cos(a), 0)
                row(0, 0, 1)
            }
        }

        /**
         * 从矩阵反解旋转角度
         *
         * 例如按照 X-Z-X 顺序，旋转 (90, -90, 0) 与 (-90, 90, -180) 等价，
         * 因此会有两组解。
         *
         * @return 较小的那组解
         */
        fun fromMatrix(matrix: Matrix, axesOrder: AxesOrder): Angle3D {
            val r1 = solve(matrix, axesOrder, false)
            val r2 = solve(matrix, axesOrder, true)

            fun Angle3D.mag() = sqrt(
                first.asRadian() * first.asRadian()
                    + second.asRadian() * second.asRadian()
                    + third.asRadian() * third.asRadian())
            return if (r1.mag() <= r2.mag())
                r1
            else r2
        }

        private fun solve(matrix: Matrix, axesOrder: AxesOrder, secondAngleScope: Boolean): Angle3D {

            // 万向节锁，失去一个自由度
            val gimbal: Double
            val firstAngle: Double
            val secondAngle: Double
            val thirdAngle: Double

            when (axesOrder) {
                XZX -> {
                    gimbal = matrix[0, 0]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[2, 1] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[2, 1], matrix[1, 1]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 2] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[2, 2] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[1, 2], matrix[2, 2]))
                        }
                        else                       -> {
                            /*  matrix[0, 0] == cos(secondAngle)  */
                            secondAngle = (if (secondAngleScope) acos(matrix[0, 0]) else -acos(matrix[0, 0]))
                            /*  matrix[0, 2] == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix[0, 1] == -(cos(firstAngle) * sin(secondAngle))  */
                            firstAngle = atan2(matrix[0, 2] / sin(secondAngle), -matrix[0, 1] / sin(secondAngle))
                            /*  matrix[2, 0] == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[1, 0] == cos(thirdAngle) * sin(secondAngle)  */
                            thirdAngle = atan2(matrix[2, 0] / sin(secondAngle), matrix[1, 0] / sin(secondAngle))
                        }
                    }
                }
                XYX -> {
                    gimbal = matrix[0, 0]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[2, 1] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[2, 1], matrix[1, 1]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 2] == -sin(firstAngle - thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(-matrix[1, 2], matrix[1, 1]))
                        }
                        else                       -> {
                            /*  matrix[0, 0] == cos(secondAngle)  */
                            secondAngle = (if (secondAngleScope) acos(matrix[0, 0]) else -acos(matrix[0, 0]))
                            /*  matrix[0, 1] == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix[0, 2] == cos(firstAngle) * sin(secondAngle)  */
                            firstAngle = atan2(matrix[0, 1] / sin(secondAngle), matrix[0, 2] / sin(secondAngle))
                            /*  matrix[1, 0] == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[2, 0] == -(cos(thirdAngle) * sin(secondAngle))  */
                            thirdAngle = atan2(matrix[1, 0] / sin(secondAngle), -matrix[2, 0] / sin(secondAngle))
                        }
                    }
                }
                YXY -> {
                    gimbal = matrix[1, 1]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 2], matrix[0, 0]))
                        }
                        else                       -> {
                            /*  matrix[1, 1] == cos(secondAngle)  */
                            secondAngle = (if (secondAngleScope) acos(matrix[1, 1]) else -acos(matrix[1, 1]))
                            /*  matrix[1, 0] == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix[1, 2] == -(cos(firstAngle) * sin(secondAngle))  */
                            firstAngle = atan2(matrix[1, 0] / sin(secondAngle), -matrix[1, 2] / sin(secondAngle))
                            /*  matrix[0, 1] == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[2, 1] == cos(thirdAngle) * sin(secondAngle)  */
                            thirdAngle = atan2(matrix[0, 1] / sin(secondAngle), matrix[2, 1] / sin(secondAngle))
                        }
                    }
                }
                YZY -> {
                    gimbal = matrix[1, 1]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == -sin(firstAngle - thirdAngle)  */
                            /*  matrix[2, 2] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(-matrix[0, 2], matrix[2, 2]))
                        }
                        else                       -> {
                            /*  matrix[1, 1] == cos(secondAngle)  */
                            secondAngle = (if (secondAngleScope) acos(matrix[1, 1]) else -acos(matrix[1, 1]))
                            /*  matrix[1, 2] == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix[1, 0] == cos(firstAngle) * sin(secondAngle)  */
                            firstAngle = atan2(matrix[1, 2] / sin(secondAngle), matrix[1, 0] / sin(secondAngle))
                            /*  matrix[2, 1] == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[0, 1] == -(cos(thirdAngle) * sin(secondAngle))  */
                            thirdAngle = atan2(matrix[2, 1] / sin(secondAngle), -matrix[0, 1] / sin(secondAngle))
                        }
                    }
                }
                ZYZ -> {
                    gimbal = matrix[2, 2]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 0] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[1, 0], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 1] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 1], matrix[1, 1]))
                        }
                        else                       -> {
                            /*  matrix[2, 2] == cos(secondAngle)  */
                            secondAngle = (if (secondAngleScope) acos(matrix[2, 2]) else -acos(matrix[2, 2]))
                            /*  matrix[2, 1] == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix[2, 0] == -(cos(firstAngle) * sin(secondAngle))  */
                            firstAngle = atan2(matrix[2, 1] / sin(secondAngle), -matrix[2, 0] / sin(secondAngle))
                            /*  matrix[1, 2] == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[0, 2] == cos(thirdAngle) * sin(secondAngle)  */
                            thirdAngle = atan2(matrix[1, 2] / sin(secondAngle), matrix[0, 2] / sin(secondAngle))
                        }
                    }
                }
                ZXZ -> {
                    gimbal = matrix[2, 2]  /*  cos(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = .0
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 0] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[1, 0], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = PI
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 1] == -sin(firstAngle - thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(-matrix[0, 1], matrix[0, 0]))
                        }
                        else                       -> {
                            /*  matrix[2, 2] == cos(secondAngle)  */
                            secondAngle = (if (secondAngleScope) acos(matrix[2, 2]) else -acos(matrix[2, 2]))
                            /*  matrix[2, 0] == sin(firstAngle) * sin(secondAngle)  */
                            /*  matrix[2, 1] == cos(firstAngle) * sin(secondAngle)  */
                            firstAngle = atan2(matrix[2, 0] / sin(secondAngle), matrix[2, 1] / sin(secondAngle))
                            /*  matrix[0, 2] == sin(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[1, 2] == -(cos(thirdAngle) * sin(secondAngle))  */
                            thirdAngle = atan2(matrix[0, 2] / sin(secondAngle), -matrix[1, 2] / sin(secondAngle))
                        }
                    }
                }
                XZY -> {
                    gimbal = matrix[1, 0]  /*  sin(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[2, 2] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[2, 2]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[2, 1] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[0, 1] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[2, 1], matrix[0, 1]))
                        }
                        else                       -> {
                            /*  matrix[1, 0] == sin(secondAngle)  */
                            secondAngle = (if (secondAngleScope) asin(matrix[1, 0]) else PI - asin(matrix[1, 0]))
                            /*  matrix[1, 2] == -(cos(secondAngle) * sin(firstAngle))  */
                            /*  matrix[1, 1] == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(-matrix[1, 2] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                            /*  matrix[2, 0] == -(cos(secondAngle) * sin(thirdAngle))  */
                            /*  matrix[0, 0] == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(-matrix[2, 0] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                        }
                    }
                }
                XYZ -> {
                    gimbal = matrix[2, 0]  /*  -sin(secondAngle)  */
                    when {
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 1] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[0, 2] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 1], matrix[0, 2]))
                        }
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 1] == -sin(firstAngle + thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(-matrix[0, 1], matrix[1, 1]) - firstAngle)
                        }
                        else                       -> {
                            /*  matrix[2, 0] == -sin(secondAngle)  */
                            secondAngle = (if (secondAngleScope) -asin(matrix[2, 0]) else PI + asin(matrix[2, 0]))
                            /*  matrix[2, 1] == cos(secondAngle) * sin(firstAngle)  */
                            /*  matrix[2, 2] == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(matrix[2, 1] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                            /*  matrix[1, 0] == cos(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[0, 0] == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(matrix[1, 0] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                        }
                    }
                }
                YXZ -> {
                    gimbal = matrix[2, 1]  /*  sin(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[0, 2], matrix[0, 0]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 2] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[0, 2], matrix[0, 0]))
                        }
                        else                       -> {
                            /*  matrix[2, 1] == sin(secondAngle)  */
                            secondAngle = (if (secondAngleScope) asin(matrix[2, 1]) else PI - asin(matrix[2, 1]))
                            /*  matrix[2, 0] == -(cos(secondAngle) * sin(firstAngle))  */
                            /*  matrix[2, 2] == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(-matrix[2, 0] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                            /*  matrix[0, 1] == -(cos(secondAngle) * sin(thirdAngle))  */
                            /*  matrix[1, 1] == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(-matrix[0, 1] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                        }
                    }
                }
                YZX -> {
                    gimbal = matrix[0, 1]  /*  -sin(secondAngle)  */
                    when {
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 2] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[1, 0] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[1, 2], matrix[1, 0]))
                        }
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 2) == -sin(firstAngle + thirdAngle)  */
                            /*  matrix[2, 2) == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(-matrix[1, 2], matrix[2, 2]) - firstAngle)
                        }
                        else                       -> {
                            /*  matrix[0, 1] == -sin(secondAngle)  */
                            secondAngle = (if (secondAngleScope) -asin(matrix[0, 1]) else PI + asin(matrix[0, 1]))
                            /*  matrix[0, 2] == cos(secondAngle) * sin(firstAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(matrix[0, 2] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                            /*  matrix[2, 1] == cos(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[1, 1] == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(matrix[2, 1] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                        }
                    }
                }
                ZYX -> {
                    gimbal = matrix[0, 2]  /*  sin(secondAngle)  */
                    when {
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 0] == sin(firstAngle + thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(matrix[1, 0], matrix[1, 1]) - firstAngle)
                        }
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[1, 0] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[1, 0], matrix[1, 1]))
                        }
                        else                       -> {
                            /*  matrix[0, 2] == sin(secondAngle)  */
                            secondAngle = (if (secondAngleScope) asin(matrix[0, 2]) else PI - asin(matrix[0, 2]))
                            /*  matrix[0, 1] == -(cos(secondAngle) * sin(firstAngle))  */
                            /*  matrix[0, 0] == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(-matrix[0, 1] / cos(secondAngle), matrix[0, 0] / cos(secondAngle))
                            /*  matrix[1, 2] == -(cos(secondAngle) * sin(thirdAngle))  */
                            /*  matrix[2, 2] == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(-matrix[1, 2] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                        }
                    }
                }
                ZXY -> {
                    gimbal = matrix[1, 2]  /*  -sin(secondAngle)  */
                    when {
                        doubleEquals(gimbal, -1.0) -> {
                            secondAngle = (PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[2, 0] == sin(firstAngle - thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle - thirdAngle)  */
                            thirdAngle = (firstAngle - atan2(matrix[2, 0], matrix[0, 0]))
                        }
                        doubleEquals(gimbal, 1.0)  -> {
                            secondAngle = (-PI / 2)
                            firstAngle = .0  /*  arbitrary  */
                            /*  matrix[0, 1] == -sin(firstAngle + thirdAngle)  */
                            /*  matrix[0, 0] == cos(firstAngle + thirdAngle)  */
                            thirdAngle = (atan2(-matrix[0, 1], matrix[0, 0]) - firstAngle)
                        }
                        else                       -> {
                            /*  matrix[1, 2] == -sin(secondAngle)  */
                            secondAngle = (if (secondAngleScope) -asin(matrix[1, 2]) else PI + asin(matrix[1, 2]))
                            /*  matrix[1, 0] == cos(secondAngle) * sin(firstAngle)  */
                            /*  matrix[1, 1] == cos(firstAngle) * cos(secondAngle)  */
                            firstAngle = atan2(matrix[1, 0] / cos(secondAngle), matrix[1, 1] / cos(secondAngle))
                            /*  matrix[0, 2] == cos(secondAngle) * sin(thirdAngle)  */
                            /*  matrix[2, 2] == cos(secondAngle) * cos(thirdAngle)  */
                            thirdAngle = atan2(matrix[0, 2] / cos(secondAngle), matrix[2, 2] / cos(secondAngle))
                        }
                    }
                }
            }
            return Angle3D(
                firstAngle.toRad(),
                secondAngle.toRad(),
                thirdAngle.toRad(),
                axesOrder)
        }
    }

    override fun hashCode(): Int = matrix.hashCode()

    override fun equals(other: Any?): Boolean =
        other is Angle3D && other.matrix == matrix
}
