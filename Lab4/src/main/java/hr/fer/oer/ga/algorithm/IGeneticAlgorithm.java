package hr.fer.oer.ga.algorithm;

import java.util.List;

/**
 * @author matejc
 * Created on 16.11.2022.
 */

@FunctionalInterface
public interface IGeneticAlgorithm<T> {
    T runGA(List<T> startingPopulation);
}
