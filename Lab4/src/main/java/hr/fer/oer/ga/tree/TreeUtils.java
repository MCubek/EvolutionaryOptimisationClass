package hr.fer.oer.ga.tree;

import hr.fer.oer.ga.model.Configuration;
import hr.fer.oer.ga.model.Pair;
import hr.fer.oer.ga.nodes.*;

import java.util.*;

import static hr.fer.oer.ga.tree.TreeGenerationMethod.FULL;
import static hr.fer.oer.ga.tree.TreeGenerationMethod.GROW;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class TreeUtils {
    private TreeUtils() {
    }

    private static final Random random = new Random();


    public static Node getRandomNode(Node root) {
        int depth = random.nextInt(1, root.getDepth());

        Node currentNode = root;

        while (depth > 0 && currentNode.getNumberOfChildren() != 0) {
            currentNode = getRandomElement(currentNode.getChildren());
            depth -= 1;
        }

        return currentNode;
    }

    public static Pair<ValueNode> randomCrossNodes(ValueNode first, ValueNode second, Configuration configuration) {
        ValueNode firstCopy = first.copyNode(null);
        ValueNode secondCopy = second.copyNode(null);

        int tries = 0;

        Node firstPick;
        Node secondPick;

        do {
            if (tries >= 20) return new Pair<>(firstCopy, secondCopy);

            firstPick = getRandomNode(firstCopy);
            secondPick = getRandomNode(secondCopy);

            tries += 1;
        } while (! (canNodeBeInserted(firstPick.numNodesToHead(), secondPick.getDepth(), configuration.maxTreeDepth()) &&
                    canNodeBeInserted(secondPick.numNodesToHead(), firstPick.getDepth(), configuration.maxTreeDepth())));

        Node firstParent = firstPick.getParent();
        Node secondParent = secondPick.getParent();

        firstParent.removeChild(firstPick);
        secondParent.removeChild(secondPick);

        firstPick.setParent(secondParent);
        secondParent.addChildNode(firstPick);

        secondPick.setParent(firstParent);
        firstParent.addChildNode(secondPick);

        return new Pair<>(firstCopy, secondCopy);
    }

    private static boolean canNodeBeInserted(int removedBranchToHead, int newBranchDepth, int maxDepth) {
        return maxDepth >= removedBranchToHead + newBranchDepth;
    }

    public static ValueNode mutateNode(ValueNode node, List<NonTerminalNode> functions, List<TerminalVariableNode> variableNodes, Configuration configuration) {
        node = node.copyNode(null);

        Node pick = getRandomNode(node);
        Node parent = pick.getParent();

        int maxDepth = configuration.maxTreeDepth() - pick.numNodesToHead();

        int depth = maxDepth != 1 ? random.nextInt(1, maxDepth) : 1;

        Node newBranch = createRandomTree(GROW, depth, configuration, functions, variableNodes, true);

        parent.removeChild(pick);
        parent.addChildNode(newBranch);
        newBranch.setParent(parent);

        return node;
    }

    public static ValueNode createRandomTree(TreeGenerationMethod method,
                                             int maxDepth,
                                             Configuration configuration,
                                             List<NonTerminalNode> functions,
                                             List<TerminalVariableNode> terminalVariableNodes,
                                             boolean isMutation) {

        if (maxDepth == 1) return getRandomTerminalNode(terminalVariableNodes, configuration);

        ValueNode head = (isMutation ? getRandomNodeFromWholeSet(functions, terminalVariableNodes, configuration)
                : getRandomElement(functions))
                .copyNode(null);

        if (head instanceof TerminalNode) return head;


        Deque<NonTerminalNode> stack = new ArrayDeque<>();
        stack.add((NonTerminalNode) head);

        while (! stack.isEmpty()) {
            NonTerminalNode currentHead = stack.pop();

            for (int i = 0; i < currentHead.getNumberOfVariables(); i++) {
                if (currentHead.numNodesToHead() == maxDepth - 2) {
                    Node terminalNode = getRandomTerminalNode(terminalVariableNodes, configuration)
                            .copyNode(currentHead);

                    currentHead.addChildNode(terminalNode);
                } else {
                    Node newNode = method == FULL ? getRandomElement(functions) : getRandomNodeFromWholeSet(functions, terminalVariableNodes, configuration);
                    newNode = newNode.copyNode(currentHead);

                    currentHead.addChildNode(newNode);

                    if (newNode instanceof NonTerminalNode nonTerminalNode)
                        stack.push(nonTerminalNode);
                }
            }
        }
        return head;
    }

    public static List<ValueNode> createRampedHalfAndHalfPopulation(Configuration configuration, int maxDepth, List<NonTerminalNode> functions, List<TerminalVariableNode> variables) {
        int populationSize = configuration.populationSize();

        int differentDepths = maxDepth - (int) Math.ceil(maxDepth / 2.0);
        int treesPerDepthAndType = (int) Math.floor(1.0 * populationSize / differentDepths / 2);

        List<ValueNode> population = new ArrayList<>();

        for (int depth = maxDepth - differentDepths + 1; depth <= maxDepth; depth++) {
            for (int i = 0; i < treesPerDepthAndType; i++) {
                population.add(createRandomTree(FULL, depth, configuration, functions, variables, false));
            }
            for (int i = 0; i < treesPerDepthAndType; i++) {
                population.add(createRandomTree(GROW, depth, configuration, functions, variables, false));
            }
        }

        while (population.size() < populationSize) {
            population.add(createRandomTree(GROW, maxDepth, configuration, functions, variables, false));
        }
        return population;
    }

    private static TerminalNode getRandomTerminalNode(List<TerminalVariableNode> variables, Configuration configuration) {
        if (! configuration.constants()) return getRandomElement(variables);

        if (random.nextBoolean()) return getRandomElement(variables);

        else
            return new TerminalConstantNode(null, random.nextDouble(configuration.minConstant(), configuration.maxConstant()));
    }

    private static ValueNode getRandomNodeFromWholeSet(List<NonTerminalNode> functions, List<TerminalVariableNode> variables, Configuration configuration) {
        if (random.nextBoolean()) return getRandomElement(functions);

        else return getRandomTerminalNode(variables, configuration);
    }

    public static <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static <T> List<T> pickNRandomElements(List<T> list, int n) {
        List<T> copy = new ArrayList<>(list);
        List<T> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(copy.size());
            T element = copy.get(index);
            result.add(element);
            copy.remove(index);
        }
        return result;
    }
}
