import cn.berberman.algebra.fraction.toFraction
import org.junit.Assert
import org.junit.Test

class TestFraction {
	@Test
	fun testFromString() {
		val a = "-3/3".toFraction()
		val b = "4/-3".toFraction()

		Assert.assertEquals("4/3".toFraction(), a * b)
	}
}