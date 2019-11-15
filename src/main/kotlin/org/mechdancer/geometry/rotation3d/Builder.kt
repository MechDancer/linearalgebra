package org.mechdancer.geometry.rotation3d

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.geometry.angle.Angle

/**
 * 构造内旋 [Angle3D]
 */
fun euler(first: Angle, second: Angle, third: Angle, axesOrder: AxesOrder = AxesOrder.ZYZ) =
    Angle3D(first, second, third, axesOrder.reverse())

/**
 * 构造外旋 [Angle3D]
 */
fun rollPitchYaw(first: Angle, second: Angle, third: Angle, axesOrder: AxesOrder = AxesOrder.XYZ) =
    Angle3D(first, second, third, axesOrder)

/**
 * 从矩阵反解旋转角度
 *
 * 请见 [Angle3D.fromMatrix]
 */
fun Matrix.toRollPitchYaw(axesOrder: AxesOrder) =
    Angle3D.fromMatrix(this, axesOrder)

/**
 * 从矩阵反解旋转角度
 *
 * 请见 [Angle3D.fromMatrix]
 */
fun Matrix.toEuler(axesOrder: AxesOrder) =
    Angle3D.fromMatrix(this, axesOrder.reverse())

