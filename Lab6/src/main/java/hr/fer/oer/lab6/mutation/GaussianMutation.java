package hr.fer.oer.lab6.mutation;

import hr.fer.oer.lab6.problem.MOOPProblem;

import java.util.Random;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class GaussianMutation implements IMutation {
    private final MOOPProblem problem;
    private final Random random = new Random();

    private static final double PROBABILITY = 0.2;


    public GaussianMutation(MOOPProblem problem) {
        this.problem = problem;
    }

    @Override
    public void mutate(double[] solution) {
        for (int i = 0; i < solution.length; i++) {
            if (random.nextDouble() < PROBABILITY) {
                double max = problem.getMaxValues()[i];
                double min = problem.getMinValues()[i];

                double stddev = (max - min) / 5;

                double value = solution[i] + random.nextGaussian(0, stddev);

                solution[i] = problem.makeFitInLimits(i, value);
            }
        }
    }
}
