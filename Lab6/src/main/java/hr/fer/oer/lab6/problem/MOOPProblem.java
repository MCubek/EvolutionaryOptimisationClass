package hr.fer.oer.lab6.problem;

/**
 * @author matejc
 * Created on 29.12.2022.
 */


public interface MOOPProblem {
    int getNumberOfObjectives();

    int getNumberOfVariables();

    double[] evaluateSolution(double[] solution, double[] objectives);

    double[] getMinValues();

    double[] getMaxValues();

    double makeFitInLimits(int index, double value);
}

