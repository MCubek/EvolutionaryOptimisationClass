package hr.fer.zemirs.oer.dz3.algorithm.simulated_annaeling;

import hr.fer.zemirs.oer.dz3.algorithm.FunctionStrategy;
import hr.fer.zemirs.oer.dz3.algorithm.IAlgorithm;
import hr.fer.zemirs.oer.dz3.algorithm.simulated_annaeling.temperature.ITemperatureStrategy;
import hr.fer.zemirs.oer.dz3.function.IFunction;
import hr.fer.zemirs.oer.dz3.solution.Solution;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;
import java.util.Random;

/**
 * @author matejc
 * Created on 04.11.2022.
 */

public class SimulatedAnnealing implements IAlgorithm<Solution> {
    private final IFunction function;
    private final ITemperatureStrategy temperatureStrategy;
    private final FunctionStrategy functionStrategy;

    private final Random random;

    public SimulatedAnnealing(IFunction function, ITemperatureStrategy temperatureStrategy, FunctionStrategy functionStrategy, Random random) {
        this.function = function;
        this.temperatureStrategy = temperatureStrategy;
        this.functionStrategy = functionStrategy;
        this.random = random;
    }

    @Override
    public Solution run(Solution startingPoint) {

        Solution current = Objects.requireNonNull(startingPoint);

        temperatureStrategy.initialiseCurrentTemperature();

        double temperature = Double.MAX_VALUE;

        for (int i = 0; temperature > temperatureStrategy.minTemperature(); i++) {
            temperature = temperatureStrategy.getAndUpdateTemperature();

            for (int j = 0; j < temperatureStrategy.innerLoopCount(); j++) {

                Solution neighbour = current.getRandomNeighbour();

                current = doSwitchWithNeighbour(current, neighbour, temperature) ? neighbour : current;
            }
            if (i % 10 == 0) {
                System.out.printf("Iteration %d, temperature: %f, solution: %s, value: %f%n", i, temperature, current, function.valueAt(current));
            }
        }
        return current;
    }

    private boolean doSwitchWithNeighbour(RealVector current, RealVector neighbour, double temperature) {
        double currentFitness = function.valueAt(current);
        double neighbourFitness = function.valueAt(neighbour);

        int strategyMultiplier = switch (functionStrategy) {
            case MINIMISE -> 1;
            case MAXIMISE -> - 1;
        };

        double deltaE = (neighbourFitness - currentFitness) * strategyMultiplier;

        if (deltaE <= 0) return true;

        return random.nextDouble() <= Math.exp(- deltaE / temperature);
    }
}
