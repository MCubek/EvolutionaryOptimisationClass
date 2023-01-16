package hr.fer.zemirs.oer.dz3.function;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.stream.IntStream;

/**
 * @author matejc
 * Created on 16.10.2022.
 */

@SuppressWarnings("DuplicatedCode")
public class TransferableFunction implements IFunction {

    private final RealMatrix coefficients;
    private final RealVector values;

    public TransferableFunction(RealMatrix coefficients, RealVector values) {
        this.coefficients = coefficients;
        this.values = values;
    }

    @Override
    public int getNumberOfVariables() {
        return 6;
    }

    @Override
    public double valueAt(RealVector point) {
        double a = point.getEntry(0);
        double b = point.getEntry(1);
        double c = point.getEntry(2);
        double d = point.getEntry(3);
        double e = point.getEntry(4);
        double f = point.getEntry(5);

        return IntStream.range(0, coefficients.getRowDimension())
                .mapToDouble(i -> {
                    var row = coefficients.getRowVector(i);
                    double y = values.getEntry(i);

                    double x1 = row.getEntry(0);
                    double x2 = row.getEntry(1);
                    double x3 = row.getEntry(2);
                    double x4 = row.getEntry(3);
                    double x5 = row.getEntry(4);

                    double val = a * x1 +
                                 b * x1 * x1 * x1 * x2 +
                                 c * Math.exp(d * x3) * (1 + Math.cos(e * x4)) +
                                 f * x4 * x5 * x5 -
                                 y;
                    return val * val;
                }).sum();
    }

}
