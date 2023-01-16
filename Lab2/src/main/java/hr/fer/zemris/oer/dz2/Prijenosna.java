package hr.fer.zemris.oer.dz2;

import hr.fer.zemris.oer.dz2.function.FunctionFactory;
import hr.fer.zemris.oer.dz2.function.FunctionStrategy;
import hr.fer.zemris.oer.dz2.function.IFunction;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author matejc
 * Created on 16.10.2022.
 */

public class Prijenosna {
    public static final double POINT_RANGE_LOWER = - 6;
    public static final double POINT_RANGE_HIGHER = 6;

    public static final int VARIABLES = 6;

    public static void main(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException("Expected 2 arguments got %d.".formatted(args.length));

        int iterations = Integer.parseInt(args[0]);
        Path file = Path.of(args[1]);

        try {
            IFunction function = Objects.requireNonNull(FunctionFactory.fromFile(file, FunctionStrategy.TRANSFERABLE, VARIABLES));

            RealVector randomVector = PointUtil.randomVector(POINT_RANGE_LOWER, POINT_RANGE_HIGHER, VARIABLES);
            // RealVector solutionPoint = new ArrayRealVector(new double[]{7, - 3, 2, 1, 3, 3});

            RealVector solution = NumOptAlgorithms.gradientDescent(function, iterations, randomVector);

            System.out.printf("Solution found: %s.%n", PointUtil.formatVectorString(solution));
            System.out.printf("Function value of solution = %f.%n", function.valueAt(solution));

        } catch (IOException e) {
            System.err.printf("Error while reading file: %s", e.getMessage());
        }
    }
}
