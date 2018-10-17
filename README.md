# Kotlin 线性代数库
[![Build Status](https://www.travis-ci.org/MechDancer/linearalgebra.svg?branch=master)](https://www.travis-ci.org/MechDancer/linearalgebra)

支持线性代数各类常用判定和计算，以及线性空间内的一些解析几何计算问题。

## 类型

库主要支持三类线性代数的数学结构，`向量` 、`矩阵` 和 `线性方程组`。出于性能考虑，向量和矩阵各自有多种实现，用户需要从中选择一种。

### 向量

目前向量提供两种实现：

* 列表向量 `ListVector`

* 二维向量 `Vector2D`

列表向量的数据存在一个 `List<Double>` 中，可以通过成员 `data` 直接访问。0 维向量也是有效的。

二维向量是特化为二维的向量，支持一些平面几何的相关操作。两个二维向量进行返回向量的二元运算时返回的仍是二维向量。处理平面几何问题时使用这种实现可以避免许多不必要的维数检查。

这两种实现都是不可变的。

### 矩阵

矩阵的很多操作只修改矩阵内很少的额元素，此时原地计算将具有很大的性能优势。为了支持这些操作，矩阵有两种接口：不可变的 `Matrix` 和元素值可变的 `ValueMutableMatrix`。库为这两种接口分别提供了一种实现：

|         接口        |    实现     |  直接访问数据  |
| :----------------: | :---------: | :----------: |
|       Matrix       | ListMatrix  |     data     |
| ValueMutableMatrix | ArrayMatrix |     data     |

### 方程组

目前方程组并无实现的必要，因此只是一些类型别名。

```kotlin
typealias Equation            = Pair<Vector, Number> //方程包括参数向量和常数项
typealias EquationSet         = Set<Equation>        //方程组是方程的集合
typealias EquationSetOfMatrix = Pair<Matrix, Vector> //方程组也可以写成增广矩阵的形式
```

* 注意：由于 Java 不支持 Kotlin 的类型别名，因此在 Java 中调用库需要使用这些类型的本名。
* 扩展方法在 Java 中会表现为有一个参数的静态函数。

## 录入和构造

借助丰富的 DSL 函数，用户可以选择多种录入风格。

### 向量

* 可变参数函数：`listVectorOf(x, y, z)`

* n 维零向量：`listVectorOfZero(n)`

* 2 维向量：`vector2DOf(x, y)`

* 2 维零向量：`vector2DOfZero()`

* 从数字可遍历集 `Itorable<Number>` 或数字数组 `Array<Number>` 构造：

  ```kotlin
  list .toListVector()
  array.toListVector()
  ```

### 矩阵

构造矩阵的方式更多：

* 使用函数构造：

  ```kotlin
  // map := (行号, 列号) → 元素值
  listMatrixOf(row, column, map)
  arrayMatrixOf(row, column, map)
  ```

* 折叠数组或列表：

  ```kotlin
  list  foldToRowOf    3 //数字列表折叠到 3 列的不可变矩阵
  list  foldToRows     3 //数字列表折叠到 3 行的不可变矩阵
  list  foldToColumnOf 3 //数字列表折叠到 3 行的不可变矩阵
  list  foldToCloumns  3 //数字列表折叠到 3 列的不可变矩阵
  
  array foldToRowOf    3 //数字数组折叠到 3 列的值可变矩阵
  array foldToRows     3 //数字数组折叠到 3 行的值可变矩阵
  array foldToColumnOf 3 //数字数组折叠到 3 行的值可变矩阵
  array foldToCloumns  3 //数字数组折叠到 3 列的值可变矩阵
  ```

* 使用函数构造零矩阵和单位矩阵：

  ```kotlin
  listMatrixOfZero
  ```

* 构造 n 阶数值矩阵：

  ```kotlin
  2.5 toListMatrix  2 // 等价于 2.5 的 2 阶不可变数值矩阵
  2.5 toArrayMatrix 2 // 等价于 2.5 的 2 阶值可变数值矩阵
  ```

* 从向量构造矩阵：

  ```kotlin
  vector.toListMatrix()     // 将列向量转化为一列的不可变矩阵
  vector.toListMatrixRow()  // 将行向量转化为一行的不可变矩阵
  
  vector.toArrayMatrix()    // 将列向量转化为一列的值可变矩阵
  vector.toArrayMatrixRow() // 将行向量转化为一行的值可变矩阵
  ```

* 列出矩阵元素：

  ```kotlin
  // 按行构造不可变矩阵
  matrix {
    row(0, 0, 1)
    row(0, 1, 0)
    row(1, 0, 0)
  }
  
  // 按列构造值可变矩阵
  matrix(ValueMutable) {
    column(0, 0, 1)
    column(0, 1, 0)
    column(1, 0, 0)
  }
  ```

  * 元素要么按行列举，要么按列列举，混合行列将在运行时产生错误。

* 使用 “数学家” 缩写：

  ```kotlin
  val a = O(3)     // 3 x 3 零矩阵
  val b = O(5, 3)  // 5 x 3 零矩阵
  
  val c = I(2)     // 2 阶单位阵
  val d = N(6, .2) // 等价于 0.2 的 6 阶数值阵
  ```

  * 这些缩写本质是只用静态方法的单例类，由于名字非常短，很可能与其他同名同型的东西混淆，使用时应谨慎。

### 方程组

我们只为方程组提供了一种构造工具：

```kotlin
equations {         // 今有雉兔同笼
    this[1, 1] = 35 // 上有三十五头
    this[2, 4] = 94 // 下有九十四足
}.solve()           // 问雉兔各几何
```

由于所谓方程组只是方程的集合，因此可以方便地使用 `flatten()` 来合并方程组。

```kotlin
val set = listOf(set1, set2, set3).flatten().toSet()
```

由于集合 `Set` 的特性，在 `toSet()` 过程中，完全相同的方程会被消除掉。

## 运算

本线性代数库以扩展方法形式支持极其丰富的线性代数运算，未来还将越来越多。因此在此不一一列举，使用 IDEA 和 Kotlin 语言的用户请开启代码补全以查阅，或查看本工程的单元测试代码，那里提供了一些示例。

使用 Java 的用户请注意：

* 访问字段被转化为了 getXXX() 函数

* 设置字段被转化为里 setXXX(value) 函数

* 扩展函数被转化为了静态函数，可直接打开类调用

