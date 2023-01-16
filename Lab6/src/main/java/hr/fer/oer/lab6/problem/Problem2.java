package hr.fer.oer.lab6.problem;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class Problem2 implements MOOPProblem {
    @Override
    public double[] evaluateSolution(double[] solution, double[] objectives) {
        if (objectives == null) objectives = new double[solution.length];

        if (solution.length != 2 || objectives.length != 2)
            throw new IllegalArgumentException("Arrays must match problem size of 2 objectives and variables.");

        objectives[0] = solution[0];
        objectives[1] = (1 + solution[1]) / solution[0];

        return objectives;
    }

    @Override
    public double[] getMinValues() {
        return new double[]{0.1, 0};
    }

    @Override
    public double[] getMaxValues() {
        return new double[]{1, 5};
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
        return 2;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }
}
