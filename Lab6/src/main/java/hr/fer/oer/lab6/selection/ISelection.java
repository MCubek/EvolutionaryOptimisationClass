package hr.fer.oer.lab6.selection;

import hr.fer.oer.lab6.algorithm.PopulationEvaluation;

import java.util.List;

/**
 * @author matejc
 * Created on 30.12.2022.
 */

@FunctionalInterface
public interface ISelection {
    double[] select(List<double[]> population, PopulationEvaluation goodness);
}
