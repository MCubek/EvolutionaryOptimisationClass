package hr.fer.oer.ga.nodes;

import hr.fer.oer.ga.function.INonTerminalFunction;
import hr.fer.oer.ga.model.Reading;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class SingleNonTerminalNode extends NonTerminalNode {
    private final INonTerminalFunction<Double> nodeFunction;
    private final String symbol;

    public SingleNonTerminalNode(Node parent, INonTerminalFunction<Double> nodeFunction, String symbol) {
        super(parent);
        this.nodeFunction = nodeFunction;
        this.symbol = symbol;
    }

    @Override
    public double getValue(Reading reading) {
        try {
            double result = nodeFunction.apply(getChild(0).getValue(reading));
            if (! Double.isFinite(result)) result = 1;
            return result;
        } catch (ArithmeticException e) {
            return 1;
        }
    }

    @Override
    public String getNodeString() {
        return symbol;
    }

    @Override
    public ValueNode copyNode(Node parent) {
        SingleNonTerminalNode newNode = new SingleNonTerminalNode(parent, nodeFunction, symbol);

        getChildren().stream()
                .map(child -> child.copyNode(this))
                .forEach(newNode::addChildNode);

        return newNode;
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }
}
