package hr.fer.oer.ga.nodes;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public abstract class NonTerminalNode extends ValueNode {
    protected NonTerminalNode(Node parent) {
        super(parent);
    }

    public abstract int getNumberOfVariables();

    @Override
    public abstract ValueNode copyNode(Node parent);
}
