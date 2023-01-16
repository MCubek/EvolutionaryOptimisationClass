package hr.fer.zemris.oer.dz2;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author matejc
 * Created on 16.10.2022.
 */

public class PointUtil {
    private static final Random random = new Random();

    private PointUtil() {
    }


    public static RealVector randomVector(double pointRangeLower, double pointRangeHigher, int variables) {
        if (pointRangeLower > pointRangeHigher) throw new IllegalArgumentException("Min is larger than max");

        RealVector vector = new ArrayRealVector(variables);

        for (int i = 0; i < variables; i++) {
            vector.setEntry(i, random.nextDouble(pointRangeLower, pointRangeHigher));
        }

        return vector;
    }

    public static String formatVectorString(RealVector vector) {
        return Arrays.stream(vector.toArray())
                .mapToObj(val -> String.format("%.3f", val))
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
