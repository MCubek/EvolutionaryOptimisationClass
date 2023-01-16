package hr.fer.oer.ga.function;

import hr.fer.oer.ga.model.Reading;
import hr.fer.oer.ga.nodes.ValueNode;

import java.util.List;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class MeanSquareLossFunction implements ILossFunction{
    private final List<Reading> readings;

    public MeanSquareLossFunction(List<Reading> readings) {
        this.readings = readings;
    }

    @Override
    public double lossAt(ValueNode headNode) {
        return readings.stream()
                .mapToDouble(reading -> reading.fOut() - (headNode.getValue(reading) ))
                .map(x -> x * x)
                .average()
                .orElse(Double.MAX_VALUE);
    }

    @Override
    public String getStringWithModifiers(ValueNode node) {
        return node.toString();
    }
}
