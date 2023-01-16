package hr.fer.oer.ga.nodes;

import hr.fer.oer.ga.model.Reading;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class TerminalVariableNode extends TerminalNode {
    private final String variableName;
    private final int variableIndex;

    public TerminalVariableNode(Node parent, int variableIndex, String variableName) {
        super(parent);
        this.variableIndex = variableIndex;
        this.variableName = variableName;
    }

    @Override
    public String getNodeString() {
        return variableName;
    }

    @Override
    public double getValue(Reading reading) {
        return reading.xs()[variableIndex];
    }

    @Override
    public ValueNode copyNode(Node parent) {
        TerminalVariableNode newNode = new TerminalVariableNode(parent, variableIndex, variableName);

        getChildren().stream()
                .map(child -> child.copyNode(parent))
                .forEach(newNode::addChildNode);

        return newNode;
    }

}
