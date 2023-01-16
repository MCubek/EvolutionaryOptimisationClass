package hr.fer.zemris.oer.dz2;

import hr.fer.zemris.oer.dz2.function.IFunction;
import org.apache.commons.math3.linear.RealVector;

/**
 * @author matejc
 * Created on 15.10.2022.
 */

public class NumOptAlgorithms {

    private static final double ZERO_DOUBLE_PRECISION = 1E-7;

    private NumOptAlgorithms() {
    }

    public static RealVector gradientDescent(IFunction function, int iterations, RealVector startingPoint) {
        RealVector currentPoint = startingPoint;

        for (int i = 1; i < iterations; i++) {
            if (function.gradientAt(currentPoint).getNorm() < ZERO_DOUBLE_PRECISION) {
                break;
            }

            var gradient = function.gradientAt(currentPoint).unitVector().mapMultiply(- 1);

            double lambda = findLambda(function, gradient, currentPoint, iterations);

            currentPoint = currentPoint.add(gradient.mapMultiply(lambda));

            printIteration(i, currentPoint);
        }

        return currentPoint;
    }

    private static double findLambda(IFunction function, RealVector gradient, RealVector currentPoint, int iterations) {
        double lambdaLower = 0;
        double lambdaUpper = findLambdaUpper(function, gradient, currentPoint);

        double lambda = 0;

        for (int i = 0; i < iterations; i++) {
            lambda = (lambdaLower + lambdaUpper) / 2;

            RealVector nextX = currentPoint.add(gradient.mapMultiply(lambda));

            if (function.gradientAt(nextX).getNorm() < ZERO_DOUBLE_PRECISION) {
                break;
            }

            double thetaDerivative = function.gradientAt(nextX).unitVector().dotProduct(gradient);

            if (Math.abs(thetaDerivative) < ZERO_DOUBLE_PRECISION) {
                break;
            }

            if (thetaDerivative > 0) {
                lambdaUpper = lambda;
            } else {
                lambdaLower = lambda;
            }
        }

        return lambda;
    }

    private static void printIteration(int i, RealVector currentPoint) {
        System.out.printf("Iteration %d: point: %s%n", i, currentPoint);
    }

    private static double findLambdaUpper(IFunction function, RealVector gradient, RealVector currentPoint) {
        double lambda = 0.0001;

        while (true) {
            RealVector nextX = currentPoint.add(gradient.mapMultiply(lambda));
            double thetaDerivative = function.gradientAt(nextX).unitVector().dotProduct(gradient);

            if (thetaDerivative > 0) {
                return lambda;
            }

            lambda *= 2;
        }
    }
}
