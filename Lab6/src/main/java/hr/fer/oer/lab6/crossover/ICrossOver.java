package hr.fer.oer.lab6.crossover;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

@FunctionalInterface
public interface ICrossOver {
    double[] cross(double[] parent1, double[] parent2);
}
