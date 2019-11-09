package org.mechdancer.geometry.rotation3d

import org.junit.Assert
import org.junit.Test
import org.mechdancer.algebra.implement.matrix.builder.matrix
import org.mechdancer.geometry.angle.toDegree

class TestAngle3D {

    @Test
    fun testToMatrix() {
        val angle = Angle3D(
            178.412294.toDegree(),
            338.645768.toDegree(),
            313.657331.toDegree(), AxesOrder.ZYX
        )
        Assert.assertEquals(
            matrix {
                row(-0.930989, -0.025805, -0.364133)
                row(-0.244215, -0.697378, 0.673812)
                row(-0.271326, 0.716239, 0.64295)
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