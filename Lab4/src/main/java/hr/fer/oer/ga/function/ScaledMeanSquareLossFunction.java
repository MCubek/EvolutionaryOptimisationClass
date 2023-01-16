package hr.fer.oer.ga.function;

import hr.fer.oer.ga.model.Reading;
import hr.fer.oer.ga.nodes.ValueNode;

import java.util.List;

/**
 * @author matejc
 * Created on 16.11.2022.
 */

public class ScaledMeanSquareLossFunction implements ILossFunction {

    private final List<Reading> readings;

    public ScaledMeanSquareLossFunction(List<Reading> readings) {
        this.readings = readings;
    }

    @Override
    public double lossAt(ValueNode node) {
        double tSum = 0;
        double ySum = 0;

        for (var reading : readings) {
            tSum += reading.fOut();
            ySum += node.getValue(reading);
        }
        double tMean = tSum / readings.size();
        double yMean = ySum / readings.size();

        double numerator = 0;
        double denominator = 0;
        for (var reading : readings) {
            numerator += (reading.fOut() - tMean) * (node.getValue(reading) - yMean);
            denominator += Math.pow(node.getValue(reading) - yMean, 2);
        }
        double b = numerator / denominator;
        double a = tMean - b * yMean;

        return readings.stream()
                .mapToDouble(reading -> reading.fOut() - (a + b * node.getValue(reading)))
                .map(x -> x * x)
                .average()
                .orElse(Double.MAX_VALUE);
    }

    @Override
    public String getStringWithModifiers(ValueNode node) {
        double tSum = 0;
        double ySum = 0;

        for (var reading : readings) {
            tSum += reading.fOut();
            ySum += node.getValue(reading);
        }
        double tMean = tSum / readings.size();
        double yMean = ySum / readings.size();

        double numerator = 0;
        double denominator = 0;
        for (var reading : readings) {
            numerator += (reading.fOut() - tMean) * (node.getValue(reading) - yMean);
            denominator += Math.pow(node.getValue(reading) - yMean, 2);
        }
        double b = numerator / denominator;
        double a = tMean - b * yMean;

        return String.format("%s%n%.4f + %.4f X", node, a, b);
    }
}
