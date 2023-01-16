package hr.fer.oer.lab5.function;

import org.apache.commons.math3.linear.RealVector;

/**
 * @author matejc
 * Created on 13.10.2022.
 */

public interface IFunction {

    double errorAt(RealVector point);

    int numberOfVariables();

}
