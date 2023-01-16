package hr.fer.zemris.oer.dz2.function;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * @author matejc
 * Created on 16.10.2022.
 */

public class LinearFunction implements IFunction {

    private final RealMatrix matrix;
    private final RealVector vector;

    public LinearFunction(RealMatrix matrix, RealVector vector) {
        this.matrix = matrix;
        this.vector = vector;
    }

    @Override
    public int getNumberOfVariables() {
        return matrix.getColumnDimension();
    }

    // Linear least squares
    // F(x) = ||Ax-b||^2
    @Override
    public double valueAt(RealVector point) {
        double value = matrix.operate(point).subtract(vector).getNorm();

        return value * value;
    }

    // deltaF(x) = 2*A(translated)*(Ax-b)
    @Override
    public RealVector gradientAt(RealVector point) {
        return matrix.transpose().scalarMultiply(2).operate((matrix.operate(point).subtract(vector)));
    }

}
