package hr.fer.oer.lab5.function;

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

    public static IFunction fromFile(Path file, int variables) throws IOException {

        var lines = Files.readAllLines(file);

        var matrixArray = lines.stream()
                .filter(x -> !x.startsWith("#"))
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
                .filter(x -> !x.startsWith("#"))
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

        return new TransferableFunctionSixParams(matrix, vector);

    }
}
