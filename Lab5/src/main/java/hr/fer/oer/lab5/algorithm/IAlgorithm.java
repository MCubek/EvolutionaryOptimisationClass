package hr.fer.oer.lab5.algorithm;

import org.apache.commons.math3.linear.RealVector;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

@FunctionalInterface
public interface IAlgorithm {
    RealVector run();
}
