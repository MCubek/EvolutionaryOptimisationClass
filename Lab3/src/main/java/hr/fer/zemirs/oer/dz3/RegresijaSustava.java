package hr.fer.zemirs.oer.dz3;

import hr.fer.zemirs.oer.dz3.algorithm.FunctionStrategy;
import hr.fer.zemirs.oer.dz3.algorithm.IAlgorithm;
import hr.fer.zemirs.oer.dz3.algorithm.simulated_annaeling.SimulatedAnnealing;
import hr.fer.zemirs.oer.dz3.algorithm.simulated_annaeling.temperature.GeometricTemperatureStrategy;
import hr.fer.zemirs.oer.dz3.algorithm.simulated_annaeling.temperature.ITemperatureStrategy;
import hr.fer.zemirs.oer.dz3.function.FunctionFactory;
import hr.fer.zemirs.oer.dz3.function.IFunction;
import hr.fer.zemirs.oer.dz3.solution.GaussianNeighbourhoodSolution;
import hr.fer.zemirs.oer.dz3.solution.Solution;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
public class RegresijaSustava {

    public static final int MIN = - 7;
    public static final int MAX = 7;
    public static final int VARIABLES = 6;

    public static final int INNERLOOP_COUNT = 20_000;

    public static final double TEMP_START = 1_000;
    public static final double TEMP_MIN = 1E-6;
    public static final double ALPHA = 0.95;

    public static final double STDDEV = 0.25;

    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Expected 1 arguments got %d.".formatted(args.length));

        Path file = Path.of(args[0]);

        Random random = new Random();

        try {
            IFunction function = Objects.requireNonNull(FunctionFactory.fromFile(file, VARIABLES));

            ITemperatureStrategy strategy = new GeometricTemperatureStrategy(INNERLOOP_COUNT, TEMP_START, TEMP_MIN, ALPHA);

            IAlgorithm<Solution> algorithm = new SimulatedAnnealing(function, strategy, FunctionStrategy.MINIMISE, random);

            Solution startingPoint = new GaussianNeighbourhoodSolution(
                    Solution.randomVector(MIN, MAX, function.getNumberOfVariables())
                    , random, STDDEV);
            // Solution startingPoint = new SimpleNeighborhoodSolution(new ArrayRealVector(new double[]{7, - 3, 2, 1, 3, 3}));

            Solution solution = algorithm.run(startingPoint);

            System.out.printf("Solution found: %s.%n", solution);
            System.out.printf("Function value of solution = %f.%n", function.valueAt(solution));

        } catch (IOException e) {
            System.err.printf("Error while reading file: %s%n", e.getMessage());
        }
    }
}