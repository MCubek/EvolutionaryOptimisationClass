package hr.fer.zemris.oer.dz2;

import hr.fer.zemris.oer.dz2.function.FunctionFactory;
import hr.fer.zemris.oer.dz2.function.IFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class Jednostavno {

    public static final double POINT_RANGE_LOWER = - 500;
    public static final double POINT_RANGE_HIGHER = 500;


    public static void main(String[] args) {
        if (args.length != 2 && args.length != 4) throw new IllegalArgumentException("2 or 4 arguments required!");

        int functionId = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);

        RealVector startingPoint;
        if (args.length == 2) {
            startingPoint = PointUtil.randomVector(POINT_RANGE_LOWER, POINT_RANGE_HIGHER, 2);
        } else {
            startingPoint = new ArrayRealVector(new double[]{Double.parseDouble(args[2]), Double.parseDouble(args[3])});
        }

        IFunction function = switch (functionId) {
            case 1 -> FunctionFactory.function1();
            case 2 -> FunctionFactory.function2();
            default -> throw new IllegalArgumentException("No such function %s!".formatted(functionId));
        };

        RealVector solution = NumOptAlgorithms.gradientDescent(function, iterations, startingPoint);
        System.out.printf("Solution found: %s.", PointUtil.formatVectorString(solution));

    }
}