package org.mechdancer.algebra.core

/**
 * An equation is described by some parameters and a constant
 * （线性）方程由一些参数和一个常数表示
 */
typealias Equation = Pair<Vector, Double>

/**
 * A equation set is a set of some equations
 * 方程组是方程的集合
 */
typealias EquationSet = Set<Equation>

/**
 * An augmented matrix is a matrix of arguments and a vector of constants
 * Augmented Matrix Is Not A Matrix!
 * 增广矩阵是系数矩阵和常数向量的组合
 * 增广矩阵不是矩阵！
 */
typealias AugmentedMatrix = Pair<Matrix, Vector>
