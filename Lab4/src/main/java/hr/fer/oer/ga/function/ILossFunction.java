package hr.fer.oer.ga.function;

import hr.fer.oer.ga.nodes.ValueNode;

/**
 * @author matejc
 * Created on 16.11.2022.
 */
public interface ILossFunction {
    double lossAt(ValueNode node);

    String getStringWithModifiers(ValueNode node);
}
