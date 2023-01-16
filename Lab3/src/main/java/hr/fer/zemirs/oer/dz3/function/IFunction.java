package hr.fer.zemirs.oer.dz3.function;

import org.apache.commons.math3.linear.RealVector;

/**
 * @author matejc
 * Created on 13.10.2022.
 */

public interface IFunction {
    int getNumberOfVariables();

    double valueAt(RealVector point);

}
