package hr.fer.zemris.oer.dz2.function;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author matejc
 * Created on 15.10.2022.
 */

public class FunctionFactory {
    private FunctionFactory() {

    }

    public static IFunction function1() {
        return new IFunction() {
            @Override
            public int getNumberOfVariables() {
                return 2;
            }

            @Override
            public double valueAt(RealVector point) {
                double x1 = point.getEntry(0);
                double x2 = point.getEntry(1);

                return x1 * x1 + (x2 - 1) * (x2 - 1);
            }

            @Override
            public RealVector gradientAt(RealVector point) {
                double x1 = point.getEntry(0);
                double x2 = point.getEntry(1);

                double[] derivation = {2 * x1, 2 * (x2 - 1)};
                return new ArrayRealVector(derivation);
            }
        };
    }

    public static IFunction function2() {
        return new IFunction() {
            @Override
            public int getNumberOfVariables() {
                return 2;
            }

            @Override
            public double valueAt(RealVector point) {
                double x1 = point.getEntry(0);
                double x2 = point.getEntry(1);

                return (x1 - 1) * (x1 - 1) + 10 * (x2 - 2) * (x2 - 2);
            }

            @Override
            public RealVector gradientAt(RealVector point) {
                double x1 = point.getEntry(0);
                double x2 = point.getEntry(1);

                double[] derivation = {2 * (x1 - 1), 20 * (x2 - 2)};
                return new ArrayRealVector(derivation);
            }
        };
    }

    public static IFunction fromFile(Path file, FunctionStrategy strategy, int variables) throws IOException {

        var lines = Files.readAllLines(file);

        var matrixArray = lines.stream()
                .filter(x -> ! x.startsWith("#"))
                .map(line -> {
                    var stripped = line.strip();
                    stripped = stripped.substring(1, stripped.length() - 1);
                    var elements = stripped.split(",");
                    elements = Arrays.copyOf(elements, variables);
                    return Arrays.stream(elements)
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                })
                .toList()
                .toArray(new double[0][0]);

        double[] valueArray = lines.stream()
                .filter(x -> ! x.startsWith("#"))
                .map(line -> {
                    var stripped = line.strip();
                    stripped = stripped.substring(1, stripped.length() - 1);
                    var elements = stripped.split(",");
                    return elements[elements.length - 1];
                })
                .mapToDouble(Double::parseDouble)
                .toArray();

        RealMatrix matrix = new Array2DRowRealMatrix(matrixArray);
        RealVector vector = new ArrayRealVector(valueArray);

        return switch (strategy) {
            case LINEAR -> new LinearFunction(matrix, vector);
            case TRANSFERABLE -> new TransferableFunction(matrix, vector);
        };
    }
}
