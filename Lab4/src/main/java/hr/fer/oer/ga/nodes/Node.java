package hr.fer.oer.ga.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public abstract class Node {
    private final List<Node> children;

    private Node parent;

    protected Node(Node parent) {
        children = new ArrayList<>();
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public abstract String getNodeString();

    public int numNodesToHead() {
        int counter = 0;
        Node curr = this;
        while (curr.parent != null) {
            curr = curr.parent;
            counter += 1;
        }
        return counter;
    }

    public int getDepth() {
        int depth = 1;
        depth += children.stream()
                .mapToInt(Node::getDepth)
                .max()
                .orElse(0);
        return depth;
    }

    public int getNumberOfChildren() {
        return children.size();
    }

    public int getNumberOfChildrenInTree() {
        int number = 1;
        for (Node child : children) {
            number += child.getNumberOfChildrenInTree();
        }
        return number;
    }

    public void addChildNode(Node childNode) {
        children.add(childNode);
        childNode.setParent(this);
    }

    public abstract ValueNode copyNode(Node parent);

    public Node getChild(int index) {
        return children.get(index);
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    private void printTree(StringBuilder sb, String prefix, String childPrefix) {
        sb.append(prefix);
        sb.append(getNodeString());
        sb.append("\n");

        for (Iterator<Node> it = children.iterator(); it.hasNext(); ) {
            Node next = it.next();
            if (it.hasNext()) {
                next.printTree(sb, childPrefix + "├── ", childPrefix + "│   ");
            } else {
                next.printTree(sb, childPrefix + "└── ", childPrefix + "    ");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder(100);
        printTree(buff, "", "");

        return buff.toString();
    }
}
