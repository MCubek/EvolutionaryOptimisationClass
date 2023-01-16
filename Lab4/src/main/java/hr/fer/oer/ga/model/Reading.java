package hr.fer.oer.ga.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public record Reading(int variableNumber, double fOut, double... xs) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reading reading = (Reading) o;

        if (Double.compare(reading.fOut, fOut) != 0) return false;
        return Arrays.equals(xs, reading.xs);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(fOut);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.hashCode(xs);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.stream(xs)
                       .mapToObj(x -> String.format("%.3f", x))
                       .collect(Collectors.joining(" "))
               + String.format(" f: %.3f", fOut);
    }

    public static Reading parseReading(String line) {
        String[] split = line.split("\t");

        double out = Double.parseDouble(split[split.length - 1]);

        double[] xs = new double[split.length - 1];
        for (int i = 0; i < split.length - 1; i++) {
            xs[i] = Double.parseDouble(split[i]);
        }
        return new Reading(xs.length, out, xs);
    }
}
