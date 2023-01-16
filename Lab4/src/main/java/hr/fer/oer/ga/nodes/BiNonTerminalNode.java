package hr.fer.oer.ga.nodes;

import hr.fer.oer.ga.function.IBiNonTerminalFunction;
import hr.fer.oer.ga.model.Reading;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class BiNonTerminalNode extends NonTerminalNode {

    private final IBiNonTerminalFunction<Double> nodeFunction;
    private final String symbol;

    public BiNonTerminalNode(Node parent, IBiNonTerminalFunction<Double> nodeFunction, String symbol) {
        super(parent);
        this.nodeFunction = nodeFunction;
        this.symbol = symbol;
    }


    @Override
    public double getValue(Reading reading) {
        try {
            double result = nodeFunction.apply(getChild(0).getValue(reading)
                    , getChild(1).getValue(reading));
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
        BiNonTerminalNode newNode = new BiNonTerminalNode(parent, nodeFunction, symbol);

        getChildren().stream()
                .map(child -> child.copyNode(this))
                .forEach(newNode::addChildNode);

        return newNode;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }
}
