package org.mechdancer.geometry.rotation3d

enum class AxesOrder(val first: Int, val second: Int, val third: Int) {
    // Proper Euler angles
    ZYZ(3, 2, 3),
    ZXZ(3, 1, 3),
    YZY(2, 3, 2),
    YXY(2, 1, 2),
    XZX(1, 3, 1),
    XYX(1, 2, 1),
    // Tait–Bryan angles (Cardan angles)
    XYZ(1, 2, 3),
    XZY(1, 3, 2),
    YXZ(2, 1, 3),
    YZX(2, 3, 1),
    ZXY(3, 1, 2),
    ZYX(3, 2, 1);

    fun reverse() = get(third, second, first)

    companion object {
        const val X = 1
        const val Y = 2
        const val Z = 3

        operator fun get(f: Int, s: Int, t: Int) =
            values().find { it.first == f && it.second == s && it.third == t }
                ?: throw IllegalArgumentException()
    }

}