import org.junit.Assert
import org.junit.Test
import kotlin.math.sqrt

class TestVector {
	@Test
	fun test2DVector() {

		val vector2D = Vector2D(1.0, 1.0)

		Assert.assertEquals(2, vector2D.dimension)
		Assert.assertEquals(1.0, vector2D[Axis3D.X], .0)
		Assert.assertEquals(sqrt(2.0), vector2D.norm(), .0)
		Assert.assertEquals(Vector2D(3.0, 3.0), vector2D + Vector2D(2.0, 2.0))
		Assert.assertEquals(Vector2D(2.0, 2.0), vector2D * 2.0)
		Assert.assertEquals(Vector2D(-1.0, -1.0), -vector2D)
		Assert.assertEquals("2DVector(1.0,1.0)", vector2D.toString())
	}

	@Test
	fun test3DVector() {
		val vector3D = Vector3D(1.0, 1.0, 1.0)

		Assert.assertEquals(Vector3D(1.0, -2.0, 1.0), vector3D x Vector3D(3.0, 4.0, 5.0))
		Assert.assertEquals("3DVector(1.0,1.0,1.0)",vector3D.toString())
	}
}