package org.mechdancer.algebra;

import org.mechdancer.algebra.implement.equation.builder.EquationSetBuilder;

import static java.lang.Math.sqrt;
import static org.mechdancer.algebra.core.ViewKt.matrixView;
import static org.mechdancer.algebra.function.equation.SolveKt.solve;

class WorkJava {
    public static void main(String[] args) {
        var t = new EquationSetBuilder();

        t.set(new Number[]{-2, -2}, -2);
        t.set(new Number[]{sqrt(3) - 2, -2}, (sqrt(3) - 4) / 2);
        t.set(new Number[]{-2, 0}, -1 - 1.5 * 0.21);

        var set = t.getEquationSet();
        var result = solve(set);

        System.out.println(matrixView(set));
        System.out.println(result);
    }
}
