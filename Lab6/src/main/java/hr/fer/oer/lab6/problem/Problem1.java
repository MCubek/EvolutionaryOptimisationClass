package hr.fer.oer.lab6.problem;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class Problem1 implements MOOPProblem {
    @Override
    public double[] evaluateSolution(double[] solution, double[] objectives) {
        if (objectives == null) objectives = new double[solution.length];

        if (solution.length != 4 || objectives.length != 4)
            throw new IllegalArgumentException("Arrays must match problem size of 4 objectives and variables.");

        for (int i = 0; i < solution.length; i++) {
            objectives[i] = Math.pow(solution[i], 2);
        }

        return objectives;
    }

    @Override
    public double[] getMinValues() {
        return new double[]{-5, -5, -5, -5};
    }

    @Override
    public double[] getMaxValues() {
        return new double[]{5, 5, 5, 5};
    }

    @Override
    public double makeFitInLimits(int index, double value) {
        if (value < getMinValues()[index])
            return getMinValues()[index];
        else if (value > getMaxValues()[index])
            return getMaxValues()[index];
        return value;
    }

    @Override
    public int getNumberOfObjectives() {
        return 4;
    }

    @Override
    public int getNumberOfVariables() {
        return 4;
    }
}
