package hr.fer.oer.ga.nodes;

import hr.fer.oer.ga.model.Reading;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public abstract class ValueNode extends Node {
    protected ValueNode(Node parent) {
        super(parent);
    }

    public abstract double getValue(Reading reading);

    @Override
    public abstract ValueNode copyNode(Node parent);

    @Override
    public ValueNode getChild(int index) {
        return (ValueNode) super.getChild(index);
    }
}
