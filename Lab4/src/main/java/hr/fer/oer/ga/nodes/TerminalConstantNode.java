package hr.fer.oer.ga.nodes;

import hr.fer.oer.ga.model.Reading;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class TerminalConstantNode extends TerminalNode {

    private final double constant;

    public TerminalConstantNode(Node parent, double constant) {
        super(parent);
        this.constant = constant;
    }

    @Override
    public double getValue(Reading reading) {
        return constant;
    }

    @Override
    public String getNodeString() {
        return String.valueOf(constant);
    }

    @Override
    public ValueNode copyNode(Node parent) {
        TerminalConstantNode newNode = new TerminalConstantNode(parent, constant);

        getChildren().stream()
                .map(child -> child.copyNode(parent))
                .forEach(newNode::addChildNode);

        return newNode;
    }
}
