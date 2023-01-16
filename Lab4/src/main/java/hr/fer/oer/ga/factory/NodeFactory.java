package hr.fer.oer.ga.factory;

import hr.fer.oer.ga.nodes.BiNonTerminalNode;
import hr.fer.oer.ga.nodes.NonTerminalNode;
import hr.fer.oer.ga.nodes.SingleNonTerminalNode;
import hr.fer.oer.ga.nodes.TerminalVariableNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class NodeFactory {
    private NodeFactory() {
    }

    public static final BiNonTerminalNode plusNode = new BiNonTerminalNode(null, Double::sum, "+");
    public static final BiNonTerminalNode minusNode = new BiNonTerminalNode(null, (a, b) -> a - b, "-");
    public static final BiNonTerminalNode multiplicationNode = new BiNonTerminalNode(null, (a, b) -> a * b, "*");
    public static final BiNonTerminalNode divisionNode = new BiNonTerminalNode(null, (a, b) -> a / b, "/");
    public static final SingleNonTerminalNode sinNode = new SingleNonTerminalNode(null, Math::sin, "sin");
    public static final SingleNonTerminalNode cosNode = new SingleNonTerminalNode(null, Math::cos, "cos");
    public static final SingleNonTerminalNode sqrtNode = new SingleNonTerminalNode(null, Math::sqrt, "sqrt");
    public static final SingleNonTerminalNode logNode = new SingleNonTerminalNode(null, Math::log10, "log10");
    public static final SingleNonTerminalNode expNode = new SingleNonTerminalNode(null, Math::exp, "exp");

    public static List<NonTerminalNode> getFunctions() {
        return List.of(plusNode, minusNode, divisionNode, multiplicationNode, divisionNode, sinNode, cosNode, sqrtNode, logNode, expNode);
    }

    public static List<TerminalVariableNode> createVariableNodes(int numberOfVariables) {
        List<TerminalVariableNode> variableNodes = new ArrayList<>();
        for (int i = 0; i < numberOfVariables; i++) {
            variableNodes.add(new TerminalVariableNode(null, i, "x" + i));
        }
        return variableNodes;
    }
}
