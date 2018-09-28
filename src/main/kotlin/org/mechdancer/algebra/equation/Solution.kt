package org.mechdancer.algebra.equation

import org.mechdancer.algebra.matrix.Matrix

/**
 * n元一次方程组当且仅当其中线性无关的方程数目等于其解的维数时才有唯一解
 * 设独立方程数目为m，有：
 * -> m < n ∞ 欠定方程组
 * -> m = n 1 适定方程组
 * -> m > n 0 超定方程组
 * 欠定方程与适定方程的解即[Solution]
 * 表达为 a_1 * x_1 + a_2 * x_2 + ... + a_n-m * x_n-m + b = 0
 * 其中{a_1, a_2, ... , a_n-m, b}组成n行n-m+1列的矩阵
 * TODO 求解超定方程需要使用以最小二乘法为代表的某种拟合方法
 */
typealias Solution = Matrix
