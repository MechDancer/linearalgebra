package org.mechdancer.geometry.rotation3d

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.geometry.angle.toDegree
import org.mechdancer.geometry.angle3d.Angle3D
import org.mechdancer.geometry.angle3d.AxesOrder

class TestAngle3D {

    @Test
    fun testToMatrix() {
        val angle = Angle3D(
            178.412294.toDegree(),
            338.645768.toDegree(),
            313.657331.toDegree(), AxesOrder.ZYX
        )
        println(angle.matrix[0,0])
        Assert.assertEquals(
            matrix {
                row(-0.9309894234402417, -0.0258049714615563, -0.3641329384857604)
                row(-0.2442148276848184, -0.6973780476491696, 0.6738122710339335)
                row(-0.2713260241504318, 0.7162387605435082, 0.6429496298418861)
            },
            angle.matrix
        )
    }

    @Test
    fun testFromMatrix() {
        val angle = Angle3D(
            178.412294.toDegree(),
            338.645768.toDegree(),
            313.657331.toDegree(), AxesOrder.ZYX
        )
        // 旋转角表示成矩阵进行判等 旋转顺序不影响
        Assert.assertEquals(angle, Angle3D.fromMatrix(angle.matrix, AxesOrder.XZX))
    }
}