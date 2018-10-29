package org.mechdancer.algebra.function.jama

import org.mechdancer.algebra.core.Matrix
import org.mechdancer.algebra.implement.matrix.special.HilbertMatrix
import kotlin.math.hypot
import kotlin.system.measureTimeMillis

/** Eigenvalues and eigenvectors of a real matrix.
 * <P>
 * If A is symmetric, then A = V*D*V' where the eigenvalue matrix D is
 * diagonal and the eigenvector matrix V is orthogonal.
 * I.e. A = V.times(D.times(V.transpose())) and
 * V.times(V.transpose()) equals the identity matrix.
</P> * <P>
 * If A is not symmetric, then the eigenvalue matrix D is block diagonal
 * with the real eigenvalues in 1-by-1 blocks and any complex eigenvalues,
 * lambda + i*mu, in 2-by-2 blocks, [lambda, mu; -mu, lambda].  The
 * columns of V represent the eigenvectors in the sense that A*V = V*D,
 * i.e. A.times(V) equals V.times(D).  The matrix V may be badly
 * conditioned, or even singular, so the validity of the equation
 * A = V*D*inverse(V) depends upon V.cond().
</P> */

class EigenvalueDecomposition
/* ------------------------
   Constructor
 * ------------------------ */

/** Check for symmetry, then construct the eigenvalue decomposition
 * Structure to access D and V.
 * @param Arg    Square matrix
 */
(Arg: Matrix) : java.io.Serializable {

	/* ------------------------
   Class variables
 * ------------------------ */

	/** Row and column dimension (square matrix).
	 * @serial matrix dimension.
	 */
	private val n: Int

	/** Symmetry flag.
	 * @serial internal symmetry flag.
	 */
	private var issymmetric: Boolean = false

	/** Arrays for internal storage of eigenvalues.
	 * @serial internal storage of eigenvalues.
	 */
	/** Return the real parts of the eigenvalues
	 * @return     real(diag(D))
	 */

	val realEigenvalues: DoubleArray
	/** Return the imaginary parts of the eigenvalues
	 * @return     imag(diag(D))
	 */

	val imagEigenvalues: DoubleArray

	/** Array for internal storage of eigenvectors.
	 * @serial internal storage of eigenvectors.
	 */
	private val V: Array<DoubleArray>

	/** Array for internal storage of nonsymmetric Hessenberg form.
	 * @serial internal storage of nonsymmetric Hessenberg form.
	 */
	private var H: Array<DoubleArray>? = null

	/** Working storage for nonsymmetric algorithm.
	 * @serial working storage for nonsymmetric algorithm.
	 */
	private lateinit var ort: DoubleArray

	// Complex scalar division.

	@Transient
	private var cdivr: Double = 0.toDouble()
	@Transient
	private var cdivi: Double = 0.toDouble()

	/* ------------------------
   Public Methods
 * ------------------------ */

//	/** Return the eigenvector matrix
//	 * @return     V
//	 */
//
//	val v: Matrix
//		get() = Matrix(V, n, n)

	/** Return the block diagonal eigenvalue matrix
	 * @return     D
	 */

//	val d: Matrix
//		get() {
//			val X = Matrix(n, n)
//			val D = X.getArray()
//			for (i in 0 until n) {
//				for (j in 0 until n) {
//					D[i][j] = 0.0
//				}
//				D[i][i] = realEigenvalues[i]
//				if (imagEigenvalues[i] > 0) {
//					D[i][i + 1] = imagEigenvalues[i]
//				} else if (imagEigenvalues[i] < 0) {
//					D[i][i - 1] = imagEigenvalues[i]
//				}
//			}
//			return X
//		}

	/* ------------------------
   Private Methods
 * ------------------------ */

	// Symmetric Householder reduction to tridiagonal form.

	private fun tred2() {

		//  This is derived from the Algol procedures tred2 by
		//  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		//  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		for (j in 0 until n) {
			realEigenvalues[j] = V[n - 1][j]
		}

		// Householder reduction to tridiagonal form.

		for (i in n - 1 downTo 1) {

			// Scale to avoid under/overflow.

			var scale = 0.0
			var h = 0.0
			for (k in 0 until i) {
				scale = scale + Math.abs(realEigenvalues[k])
			}
			if (scale == 0.0) {
				imagEigenvalues[i] = realEigenvalues[i - 1]
				for (j in 0 until i) {
					realEigenvalues[j] = V[i - 1][j]
					V[i][j] = 0.0
					V[j][i] = 0.0
				}
			} else {

				// Generate Householder vector.

				for (k in 0 until i) {
					realEigenvalues[k] /= scale
					h += realEigenvalues[k] * realEigenvalues[k]
				}
				var f = realEigenvalues[i - 1]
				var g = Math.sqrt(h)
				if (f > 0) {
					g = -g
				}
				imagEigenvalues[i] = scale * g
				h = h - f * g
				realEigenvalues[i - 1] = f - g
				for (j in 0 until i) {
					imagEigenvalues[j] = 0.0
				}

				// Apply similarity transformation to remaining columns.

				for (j in 0 until i) {
					f = realEigenvalues[j]
					V[j][i] = f
					g = imagEigenvalues[j] + V[j][j] * f
					for (k in j + 1..i - 1) {
						g += V[k][j] * realEigenvalues[k]
						imagEigenvalues[k] += V[k][j] * f
					}
					imagEigenvalues[j] = g
				}
				f = 0.0
				for (j in 0 until i) {
					imagEigenvalues[j] /= h
					f += imagEigenvalues[j] * realEigenvalues[j]
				}
				val hh = f / (h + h)
				for (j in 0 until i) {
					imagEigenvalues[j] -= hh * realEigenvalues[j]
				}
				for (j in 0 until i) {
					f = realEigenvalues[j]
					g = imagEigenvalues[j]
					for (k in j..i - 1) {
						V[k][j] -= f * imagEigenvalues[k] + g * realEigenvalues[k]
					}
					realEigenvalues[j] = V[i - 1][j]
					V[i][j] = 0.0
				}
			}
			realEigenvalues[i] = h
		}

		// Accumulate transformations.

		for (i in 0 until n - 1) {
			V[n - 1][i] = V[i][i]
			V[i][i] = 1.0
			val h = realEigenvalues[i + 1]
			if (h != 0.0) {
				for (k in 0..i) {
					realEigenvalues[k] = V[k][i + 1] / h
				}
				for (j in 0..i) {
					var g = 0.0
					for (k in 0..i) {
						g += V[k][i + 1] * V[k][j]
					}
					for (k in 0..i) {
						V[k][j] -= g * realEigenvalues[k]
					}
				}
			}
			for (k in 0..i) {
				V[k][i + 1] = 0.0
			}
		}
		for (j in 0 until n) {
			realEigenvalues[j] = V[n - 1][j]
			V[n - 1][j] = 0.0
		}
		V[n - 1][n - 1] = 1.0
		imagEigenvalues[0] = 0.0
	}

	// Symmetric tridiagonal QL algorithm.

	private fun tql2() {

		//  This is derived from the Algol procedures tql2, by
		//  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
		//  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		for (i in 1 until n) {
			imagEigenvalues[i - 1] = imagEigenvalues[i]
		}
		imagEigenvalues[n - 1] = 0.0

		var f = 0.0
		var tst1 = 0.0
		val eps = Math.pow(2.0, -52.0)
		for (l in 0 until n) {

			// Find small subdiagonal element

			tst1 = Math.max(tst1, Math.abs(realEigenvalues[l]) + Math.abs(imagEigenvalues[l]))
			var m = l
			while (m < n) {
				if (Math.abs(imagEigenvalues[m]) <= eps * tst1) {
					break
				}
				m++
			}

			// If m == l, d[l] is an eigenvalue,
			// otherwise, iterate.

			if (m > l) {
				var iter = 0
				do {
					iter = iter + 1  // (Could check iteration count here.)

					// Compute implicit shift

					var g = realEigenvalues[l]
					var p = (realEigenvalues[l + 1] - g) / (2.0 * imagEigenvalues[l])
					var r = hypot(p, 1.0)
					if (p < 0) {
						r = -r
					}
					realEigenvalues[l] = imagEigenvalues[l] / (p + r)
					realEigenvalues[l + 1] = imagEigenvalues[l] * (p + r)
					val dl1 = realEigenvalues[l + 1]
					var h = g - realEigenvalues[l]
					for (i in l + 2 until n) {
						realEigenvalues[i] -= h
					}
					f = f + h

					// Implicit QL transformation.

					p = realEigenvalues[m]
					var c = 1.0
					var c2 = c
					var c3 = c
					val el1 = imagEigenvalues[l + 1]
					var s = 0.0
					var s2 = 0.0
					for (i in m - 1 downTo l) {
						c3 = c2
						c2 = c
						s2 = s
						g = c * imagEigenvalues[i]
						h = c * p
						r = hypot(p, imagEigenvalues[i])
						imagEigenvalues[i + 1] = s * r
						s = imagEigenvalues[i] / r
						c = p / r
						p = c * realEigenvalues[i] - s * g
						realEigenvalues[i + 1] = h + s * (c * g + s * realEigenvalues[i])

						// Accumulate transformation.

						for (k in 0 until n) {
							h = V[k][i + 1]
							V[k][i + 1] = s * V[k][i] + c * h
							V[k][i] = c * V[k][i] - s * h
						}
					}
					p = -s * s2 * c3 * el1 * imagEigenvalues[l] / dl1
					imagEigenvalues[l] = s * p
					realEigenvalues[l] = c * p

					// Check for convergence.

				} while (Math.abs(imagEigenvalues[l]) > eps * tst1)
			}
			realEigenvalues[l] = realEigenvalues[l] + f
			imagEigenvalues[l] = 0.0
		}

		// Sort eigenvalues and corresponding vectors.

		for (i in 0 until n - 1) {
			var k = i
			var p = realEigenvalues[i]
			for (j in i + 1 until n) {
				if (realEigenvalues[j] < p) {
					k = j
					p = realEigenvalues[j]
				}
			}
			if (k != i) {
				realEigenvalues[k] = realEigenvalues[i]
				realEigenvalues[i] = p
				for (j in 0 until n) {
					p = V[j][i]
					V[j][i] = V[j][k]
					V[j][k] = p
				}
			}
		}
	}

	// Nonsymmetric reduction to Hessenberg form.

	private fun orthes() {

		//  This is derived from the Algol procedures orthes and ortran,
		//  by Martin and Wilkinson, Handbook for Auto. Comp.,
		//  Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutines in EISPACK.

		val low = 0
		val high = n - 1

		for (m in low + 1..high - 1) {

			// Scale column.

			var scale = 0.0
			for (i in m..high) {
				scale = scale + Math.abs(H!![i][m - 1])
			}
			if (scale != 0.0) {

				// Compute Householder transformation.

				var h = 0.0
				for (i in high downTo m) {
					ort[i] = H!![i][m - 1] / scale
					h += ort[i] * ort[i]
				}
				var g = Math.sqrt(h)
				if (ort[m] > 0) {
					g = -g
				}
				h = h - ort[m] * g
				ort[m] = ort[m] - g

				// Apply Householder similarity transformation
				// H = (I-u*u'/h)*H*(I-u*u')/h)

				for (j in m until n) {
					var f = 0.0
					for (i in high downTo m) {
						f += ort[i] * H!![i][j]
					}
					f = f / h
					for (i in m..high) {
						H!![i][j] -= f * ort[i]
					}
				}

				for (i in 0..high) {
					var f = 0.0
					for (j in high downTo m) {
						f += ort[j] * H!![i][j]
					}
					f = f / h
					for (j in m..high) {
						H!![i][j] -= f * ort[j]
					}
				}
				ort[m] = scale * ort[m]
				H!![m][m - 1] = scale * g
			}
		}

		// Accumulate transformations (Algol's ortran).

		for (i in 0 until n) {
			for (j in 0 until n) {
				V[i][j] = if (i == j) 1.0 else 0.0
			}
		}

		for (m in high - 1 downTo low + 1) {
			if (H!![m][m - 1] != 0.0) {
				for (i in m + 1..high) {
					ort[i] = H!![i][m - 1]
				}
				for (j in m..high) {
					var g = 0.0
					for (i in m..high) {
						g += ort[i] * V[i][j]
					}
					// Double division avoids possible underflow
					g = g / ort[m] / H!![m][m - 1]
					for (i in m..high) {
						V[i][j] += g * ort[i]
					}
				}
			}
		}
	}

	private fun cdiv(xr: Double, xi: Double, yr: Double, yi: Double) {
		val r: Double
		val d: Double
		if (Math.abs(yr) > Math.abs(yi)) {
			r = yi / yr
			d = yr + r * yi
			cdivr = (xr + r * xi) / d
			cdivi = (xi - r * xr) / d
		} else {
			r = yr / yi
			d = yi + r * yr
			cdivr = (r * xr + xi) / d
			cdivi = (r * xi - xr) / d
		}
	}

	// Nonsymmetric reduction from Hessenberg to real Schur form.

	private fun hqr2() {

		//  This is derived from the Algol procedure hqr2,
		//  by Martin and Wilkinson, Handbook for Auto. Comp.,
		//  Vol.ii-Linear Algebra, and the corresponding
		//  Fortran subroutine in EISPACK.

		// Initialize

		val nn = this.n
		var n = nn - 1
		val low = 0
		val high = nn - 1
		val eps = Math.pow(2.0, -52.0)
		var exshift = 0.0
		var p = 0.0
		var q = 0.0
		var r = 0.0
		var s = 0.0
		var z = 0.0
		var t: Double
		var w: Double
		var x: Double
		var y: Double

		// Store roots isolated by balanc and compute matrix norm

		var norm = 0.0
		for (i in 0 until nn) {
			if ((i < low) or (i > high)) {
				realEigenvalues[i] = H!![i][i]
				imagEigenvalues[i] = 0.0
			}
			for (j in Math.max(i - 1, 0) until nn) {
				norm = norm + Math.abs(H!![i][j])
			}
		}

		// Outer loop over eigenvalue index

		var iter = 0
		while (n >= low) {

			// Look for single small sub-diagonal element

			var l = n
			while (l > low) {
				s = Math.abs(H!![l - 1][l - 1]) + Math.abs(H!![l][l])
				if (s == 0.0) {
					s = norm
				}
				if (Math.abs(H!![l][l - 1]) < eps * s) {
					break
				}
				l--
			}

			// Check for convergence
			// One root found

			if (l == n) {
				H!![n][n] = H!![n][n] + exshift
				realEigenvalues[n] = H!![n][n]
				imagEigenvalues[n] = 0.0
				n--
				iter = 0

				// Two roots found

			} else if (l == n - 1) {
				w = H!![n][n - 1] * H!![n - 1][n]
				p = (H!![n - 1][n - 1] - H!![n][n]) / 2.0
				q = p * p + w
				z = Math.sqrt(Math.abs(q))
				H!![n][n] = H!![n][n] + exshift
				H!![n - 1][n - 1] = H!![n - 1][n - 1] + exshift
				x = H!![n][n]

				// Real pair

				if (q >= 0) {
					if (p >= 0) {
						z = p + z
					} else {
						z = p - z
					}
					realEigenvalues[n - 1] = x + z
					realEigenvalues[n] = realEigenvalues[n - 1]
					if (z != 0.0) {
						realEigenvalues[n] = x - w / z
					}
					imagEigenvalues[n - 1] = 0.0
					imagEigenvalues[n] = 0.0
					x = H!![n][n - 1]
					s = Math.abs(x) + Math.abs(z)
					p = x / s
					q = z / s
					r = Math.sqrt(p * p + q * q)
					p = p / r
					q = q / r

					// Row modification

					for (j in n - 1 until nn) {
						z = H!![n - 1][j]
						H!![n - 1][j] = q * z + p * H!![n][j]
						H!![n][j] = q * H!![n][j] - p * z
					}

					// Column modification

					for (i in 0..n) {
						z = H!![i][n - 1]
						H!![i][n - 1] = q * z + p * H!![i][n]
						H!![i][n] = q * H!![i][n] - p * z
					}

					// Accumulate transformations

					for (i in low..high) {
						z = V[i][n - 1]
						V[i][n - 1] = q * z + p * V[i][n]
						V[i][n] = q * V[i][n] - p * z
					}

					// Complex pair

				} else {
					realEigenvalues[n - 1] = x + p
					realEigenvalues[n] = x + p
					imagEigenvalues[n - 1] = z
					imagEigenvalues[n] = -z
				}
				n = n - 2
				iter = 0

				// No convergence yet

			} else {

				// Form shift

				x = H!![n][n]
				y = 0.0
				w = 0.0
				if (l < n) {
					y = H!![n - 1][n - 1]
					w = H!![n][n - 1] * H!![n - 1][n]
				}

				// Wilkinson's original ad hoc shift

				if (iter == 10) {
					exshift += x
					for (i in low..n) {
						H!![i][i] -= x
					}
					s = Math.abs(H!![n][n - 1]) + Math.abs(H!![n - 1][n - 2])
					y = 0.75 * s
					x = y
					w = -0.4375 * s * s
				}

				// MATLAB's new ad hoc shift

				if (iter == 30) {
					s = (y - x) / 2.0
					s = s * s + w
					if (s > 0) {
						s = Math.sqrt(s)
						if (y < x) {
							s = -s
						}
						s = x - w / ((y - x) / 2.0 + s)
						for (i in low..n) {
							H!![i][i] -= s
						}
						exshift += s
						w = 0.964
						y = w
						x = y
					}
				}

				iter = iter + 1   // (Could check iteration count here.)

				// Look for two consecutive small sub-diagonal elements

				var m = n - 2
				while (m >= l) {
					z = H!![m][m]
					r = x - z
					s = y - z
					p = (r * s - w) / H!![m + 1][m] + H!![m][m + 1]
					q = H!![m + 1][m + 1] - z - r - s
					r = H!![m + 2][m + 1]
					s = Math.abs(p) + Math.abs(q) + Math.abs(r)
					p = p / s
					q = q / s
					r = r / s
					if (m == l) {
						break
					}
					if (Math.abs(H!![m][m - 1]) * (Math.abs(q) + Math.abs(r)) < eps * (Math.abs(p) * (Math.abs(H!![m - 1][m - 1]) + Math.abs(z) +
							Math.abs(H!![m + 1][m + 1])))) {
						break
					}
					m--
				}

				for (i in m + 2..n) {
					H!![i][i - 2] = 0.0
					if (i > m + 2) {
						H!![i][i - 3] = 0.0
					}
				}

				// Double QR step involving rows l:n and columns m:n

				for (k in m..n - 1) {
					val notlast = k != n - 1
					if (k != m) {
						p = H!![k][k - 1]
						q = H!![k + 1][k - 1]
						r = if (notlast) H!![k + 2][k - 1] else 0.0
						x = Math.abs(p) + Math.abs(q) + Math.abs(r)
						if (x == 0.0) {
							continue
						}
						p = p / x
						q = q / x
						r = r / x
					}

					s = Math.sqrt(p * p + q * q + r * r)
					if (p < 0) {
						s = -s
					}
					if (s != 0.0) {
						if (k != m) {
							H!![k][k - 1] = -s * x
						} else if (l != m) {
							H!![k][k - 1] = -H!![k][k - 1]
						}
						p = p + s
						x = p / s
						y = q / s
						z = r / s
						q = q / p
						r = r / p

						// Row modification

						for (j in k until nn) {
							p = H!![k][j] + q * H!![k + 1][j]
							if (notlast) {
								p = p + r * H!![k + 2][j]
								H!![k + 2][j] = H!![k + 2][j] - p * z
							}
							H!![k][j] = H!![k][j] - p * x
							H!![k + 1][j] = H!![k + 1][j] - p * y
						}

						// Column modification

						for (i in 0..Math.min(n, k + 3)) {
							p = x * H!![i][k] + y * H!![i][k + 1]
							if (notlast) {
								p = p + z * H!![i][k + 2]
								H!![i][k + 2] = H!![i][k + 2] - p * r
							}
							H!![i][k] = H!![i][k] - p
							H!![i][k + 1] = H!![i][k + 1] - p * q
						}

						// Accumulate transformations

						for (i in low..high) {
							p = x * V[i][k] + y * V[i][k + 1]
							if (notlast) {
								p = p + z * V[i][k + 2]
								V[i][k + 2] = V[i][k + 2] - p * r
							}
							V[i][k] = V[i][k] - p
							V[i][k + 1] = V[i][k + 1] - p * q
						}
					}  // (s != 0)
				}  // k loop
			}  // check convergence
		}  // while (n >= low)

		// Backsubstitute to find vectors of upper triangular form

		if (norm == 0.0) {
			return
		}

		n = nn - 1
		while (n >= 0) {
			p = realEigenvalues[n]
			q = imagEigenvalues[n]

			// Real vector

			if (q == 0.0) {
				var l = n
				H!![n][n] = 1.0
				for (i in n - 1 downTo 0) {
					w = H!![i][i] - p
					r = 0.0
					for (j in l..n) {
						r = r + H!![i][j] * H!![j][n]
					}
					if (imagEigenvalues[i] < 0.0) {
						z = w
						s = r
					} else {
						l = i
						if (imagEigenvalues[i] == 0.0) {
							if (w != 0.0) {
								H!![i][n] = -r / w
							} else {
								H!![i][n] = -r / (eps * norm)
							}

							// Solve real equations

						} else {
							x = H!![i][i + 1]
							y = H!![i + 1][i]
							q = (realEigenvalues[i] - p) * (realEigenvalues[i] - p) + imagEigenvalues[i] * imagEigenvalues[i]
							t = (x * s - z * r) / q
							H!![i][n] = t
							if (Math.abs(x) > Math.abs(z)) {
								H!![i + 1][n] = (-r - w * t) / x
							} else {
								H!![i + 1][n] = (-s - y * t) / z
							}
						}

						// Overflow control

						t = Math.abs(H!![i][n])
						if (eps * t * t > 1) {
							for (j in i..n) {
								H!![j][n] = H!![j][n] / t
							}
						}
					}
				}

				// Complex vector

			} else if (q < 0) {
				var l = n - 1

				// Last vector component imaginary so matrix is triangular

				if (Math.abs(H!![n][n - 1]) > Math.abs(H!![n - 1][n])) {
					H!![n - 1][n - 1] = q / H!![n][n - 1]
					H!![n - 1][n] = -(H!![n][n] - p) / H!![n][n - 1]
				} else {
					cdiv(0.0, -H!![n - 1][n], H!![n - 1][n - 1] - p, q)
					H!![n - 1][n - 1] = cdivr
					H!![n - 1][n] = cdivi
				}
				H!![n][n - 1] = 0.0
				H!![n][n] = 1.0
				for (i in n - 2 downTo 0) {
					var ra: Double
					var sa: Double
					var vr: Double
					val vi: Double
					ra = 0.0
					sa = 0.0
					for (j in l..n) {
						ra = ra + H!![i][j] * H!![j][n - 1]
						sa = sa + H!![i][j] * H!![j][n]
					}
					w = H!![i][i] - p

					if (imagEigenvalues[i] < 0.0) {
						z = w
						r = ra
						s = sa
					} else {
						l = i
						if (imagEigenvalues[i] == 0.0) {
							cdiv(-ra, -sa, w, q)
							H!![i][n - 1] = cdivr
							H!![i][n] = cdivi
						} else {

							// Solve complex equations

							x = H!![i][i + 1]
							y = H!![i + 1][i]
							vr = (realEigenvalues[i] - p) * (realEigenvalues[i] - p) + imagEigenvalues[i] * imagEigenvalues[i] - q * q
							vi = (realEigenvalues[i] - p) * 2.0 * q
							if ((vr == 0.0) and (vi == 0.0)) {
								vr = eps * norm * (Math.abs(w) + Math.abs(q) +
									Math.abs(x) + Math.abs(y) + Math.abs(z))
							}
							cdiv(x * r - z * ra + q * sa, x * s - z * sa - q * ra, vr, vi)
							H!![i][n - 1] = cdivr
							H!![i][n] = cdivi
							if (Math.abs(x) > Math.abs(z) + Math.abs(q)) {
								H!![i + 1][n - 1] = (-ra - w * H!![i][n - 1] + q * H!![i][n]) / x
								H!![i + 1][n] = (-sa - w * H!![i][n] - q * H!![i][n - 1]) / x
							} else {
								cdiv(-r - y * H!![i][n - 1], -s - y * H!![i][n], z, q)
								H!![i + 1][n - 1] = cdivr
								H!![i + 1][n] = cdivi
							}
						}

						// Overflow control

						t = Math.max(Math.abs(H!![i][n - 1]), Math.abs(H!![i][n]))
						if (eps * t * t > 1) {
							for (j in i..n) {
								H!![j][n - 1] = H!![j][n - 1] / t
								H!![j][n] = H!![j][n] / t
							}
						}
					}
				}
			}
			n--
		}

		// Vectors of isolated roots

		for (i in 0 until nn) {
			if ((i < low) or (i > high)) {
				for (j in i until nn) {
					V[i][j] = H!![i][j]
				}
			}
		}

		// Back transformation to get eigenvectors of original matrix

		for (j in nn - 1 downTo low) {
			for (i in low..high) {
				z = 0.0
				for (k in low..Math.min(j, high)) {
					z = z + V[i][k] * H!![k][j]
				}
				V[i][j] = z
			}
		}
	}

	init {

		val A = Array(Arg.row) { r ->
			DoubleArray(Arg.column) { c ->
				Arg[r, c]
			}
		}
		n = Arg.column
		V = Array(n) { DoubleArray(n) }
		realEigenvalues = DoubleArray(n)
		imagEigenvalues = DoubleArray(n)

		issymmetric = true
		run {
			var j = 0
			while ((j < n) and issymmetric) {
				var i = 0
				while ((i < n) and issymmetric) {
					issymmetric = A[i][j] == A[j][i]
					i++
				}
				j++
			}
		}

		if (issymmetric) {
			for (i in 0 until n) {
				for (j in 0 until n) {
					V[i][j] = A[i][j]
				}
			}

			// Tridiagonalize.
			tred2()

			// Diagonalize.
			tql2()

		} else {
			H = Array(n) { DoubleArray(n) }
			ort = DoubleArray(n)

			for (j in 0 until n) {
				for (i in 0 until n) {
					H!![i][j] = A[i][j]
				}
			}

			// Reduce to Hessenberg form.
			orthes()

			// Reduce Hessenberg to real Schur form.
			hqr2()
		}
	}
}

fun main(args: Array<String>) {
	println(measureTimeMillis {
		EigenvalueDecomposition(HilbertMatrix[400])
	})
}
