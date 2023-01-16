package hr.fer.oer.lab5;

import hr.fer.oer.lab5.algorithm.GlobalParticleSwarmAlgorithm;
import hr.fer.oer.lab5.algorithm.IAlgorithm;
import hr.fer.oer.lab5.algorithm.LocalParticleSwarmAlgorithm;
import hr.fer.oer.lab5.function.FunctionFactory;
import hr.fer.oer.lab5.function.IFunction;
import hr.fer.oer.lab5.util.PointUtil;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author matejc
 * Created on 16.10.2022.
 */

public class Runner {

    public static void main(String[] args) {
        if (args.length != 4) throw new IllegalArgumentException("Expected 4 arguments got %d.".formatted(args.length));

        int algorithmNumber = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        Path file = Path.of(args[3]);

        try {
            IFunction function = Objects.requireNonNull(FunctionFactory.fromFile(file, 6));

            IAlgorithm algorithm = switch (algorithmNumber) {
                case 1 -> new GlobalParticleSwarmAlgorithm(function, populationSize, iterations);
                case 2 -> new LocalParticleSwarmAlgorithm(function, populationSize, iterations);
                default -> throw new IllegalArgumentException("Given algorithm not found");
            };

            RealVector solution = algorithm.run();

            System.out.printf("Solution found: %s.%n", PointUtil.formatVectorString(solution));
            System.out.printf("Error of solution = %f.%n", function.errorAt(solution));

        } catch (IOException e) {
            System.err.printf("Error while reading file: %s", e.getMessage());
        }
    }
}
