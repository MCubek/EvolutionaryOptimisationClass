package hr.fer.oer.lab6.crossover;

import hr.fer.oer.lab6.problem.MOOPProblem;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class ArithmeticCrossover implements ICrossOver {
    private final MOOPProblem problem;

    private static final double LAMBDA = 0.5;

    public ArithmeticCrossover(MOOPProblem problem) {
        this.problem = problem;
    }

    @Override
    public double[] cross(double[] parent1, double[] parent2) {
        if (parent1.length != parent2.length) throw new IllegalArgumentException("Parent sizes differ.");

        double[] child = new double[parent1.length];

        for (int i = 0; i < child.length; i++) {
            double value = parent1[i] * LAMBDA + (1 - LAMBDA) * parent2[i];

            child[i] = problem.makeFitInLimits(i, value);
        }

        return child;
    }
}
