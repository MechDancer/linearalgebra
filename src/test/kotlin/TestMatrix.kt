import matrix.builder.Row
import matrix.builder.matrix
import org.junit.Assert
import org.junit.Test

class TestMatrix {

	@Test
	fun testAdd() {
		val matrix = matrix {
			Row[1.0, 2.0]
			Row[2.0, 1.0]
		}
		val matrix2 = matrix {
			Row[3.0, 2.0]
			Row[6.0, 1.0]
		}

		Assert.assertEquals(matrix {
			Row[4.0, 4.0]
			Row[8.0, 2.0]
		}, matrix + matrix2)
	}
}