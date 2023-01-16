package hr.fer.oer.ga.nodes;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public abstract class TerminalNode extends ValueNode {
    protected TerminalNode(Node parent) {
        super(parent);
    }

    @Override
    public abstract ValueNode copyNode(Node parent);
}
