# kotlin 线性代数库
[ ![Download](https://api.bintray.com/packages/mechdancer/maven/linearalgebra/images/download.svg) ](https://bintray.com/mechdancer/maven/linearalgebra/_latestVersion)
[![Build Status](https://www.travis-ci.org/MechDancer/linearalgebra.svg?branch=master)](https://www.travis-ci.org/MechDancer/linearalgebra)

大量不可变类组成的非常安全的线性代数库，支持各种向量和矩阵运算。

~~持续更新~~  
咕咕咕……

## 矩阵数据

`MatrixElement` 代表矩阵中的一行，矩阵规定为行定义。`MatrixData` 为 `List<MatrixElement>`，代表由许多行组成的一个完整矩阵数据。矩阵数据是不可变的，对应着唯一一个矩阵。`MatrixElement` 同样作为向量的数据储存容器。

### 工具类

我们提供了可变工具类和不可变工具类用来操作矩阵数据。不可变矩阵数据工具类是指如同在一个矩阵内的矩阵数据一样，它是不可变的，每次操作都会生成新的矩阵数据。可变矩阵工具类与之相反，仅在最后需要结果时才生成矩阵数据。通过工具类，可以快速进行取余子式、进行初等行变换等操作。

## 矩阵

矩阵支持的运算：

* 取元素
* 与矩阵相加（减）
* 与矩阵相乘（除）
* 与向量相乘（除）
* 数乘（除）
* 求 n 次幂
* 转为行列式（并计算）
* 初等行变换
* 求伴随矩阵
* 转置
* 阶梯化简

求逆我们提供了两种方法 —— `inverseByCompanion()`、`inverseByRowEchelon()`。

### 行列式

行列式在矩阵的基础上提供了求值功能，通过拆分余子式实现。行列式和矩阵可以无缝转化，并且拥有矩阵全部功能。

### 建立一个矩阵

#### 矩阵建造者

矩阵建造者提供了 DSL 来快速可视化建立一个矩阵，例如：

```kotlin
val matrix = matrix {
	Row[1.0, 2.0]
	Row[2.0, 1.0]
}
```

除了通过行定义外，您也可以使用 `Column[...]` 通过列定义，但我们并不推荐这样做。

#### 直接建立

如果不想使用矩阵建造者，可以先建立 `MatrixData`，也就是 `List<List<Double>>`，之后对其调用 `toMatrix()` 即可获得矩阵。除了通过 `MatrixData` 建立矩阵外，库中还提供了构造单位矩阵、零矩阵、范德蒙矩阵等常用函数。

## 向量

向量支持的运算：

* 取元素
* 与向量加（减）
* 与向量点乘
* 与向量叉乘（三维）

向量被看作矩阵的一行，同样使用 `MatrixElement` 储存数据。您可以使用顶层函数 `vectorOf(...)` 建立一个向量，也可以建立 `MatrixElement` 后对其调用 `toVector()` 获得向量。为了便利，我们特别定义了二维向量类和三维向量类。它们支持了解构，并且可以通过 `Axis3D` 获得元素。

## 线性方程组

我们通过与矩阵的结合，实现了求解线性方程组。

### 线性方程组建造者

与矩阵类似，线性方程组由 DSL 建立：

```kotlin
/*
	2x+3y-5z=3
	x-2y+z=0
	3x+y+3z=7
	 */

	private val equation = linearEquation {
		coefficient = matrix {
			Row[2.0, 3.0, -5.0]
			Row[1.0, -2.0, 1.0]
			Row[3.0, 1.0, 3.0]
		}

		constant = vectorOf(3.0, .0, 7.0)

	}
```

`coefficient` 对应方程系数，`constant` 对应等号后的常量。

### 解算器

```kotlin
@FunctionalInterface
interface Solver {
	fun solve(equation: LinearEquation): Vector
}
```

线性方程组是可被解的，这件事由解算器负责。解算器是一个函数式接口，接收一个线性方程组，返回一组解。如果试图解齐次线性方程组，程序会抛出异常。您可以自己实现解算器，我们在库中提供了两个 `CommonSolver`、`CramerSolver`。`CramerSolver` 使用克莱布法则解算方程组，这要求了矩阵是非奇异的，换句话说方程数与未知数个数必须相同。`CommonSolver` 则通过拼接增广矩阵对其进行阶梯化简实现。

## 分数



## 开始使用

* Gradle
* Maven
* Bintray

您需要将其添加至  [仓库和依赖](https://docs.gradle.org/current/userguide/declaring_dependencies.html) 中。

### Gradle

```groovy
repositories {
    jcenter()
}
dependencies {
    compile 'org.mechdancer:linearalgebra:0.1.1'
}
```

### Maven

```xml
<repositories>
   <repository>
     <id>jcenter</id>
     <name>JCenter</name>
     <url>https://jcenter.bintray.com/</url>
   </repository>
</repositories>

<dependency>
  <groupId>org.mechdancer</groupId>
  <artifactId>linearalgebra</artifactId>
  <version>0.1.1</version>
  <type>pom</type>
</dependency>
```

### Bintray

您总可以从 bintray 直接下载 jar：[ ![Download](https://api.bintray.com/packages/mechdancer/maven/linearalgebra/images/download.svg) ](https://bintray.com/mechdancer/maven/linearalgebra/_latestVersion)
