package hr.fer.oer.ga.algorithm;

import hr.fer.oer.ga.function.ILossFunction;
import hr.fer.oer.ga.function.MeanSquareLossFunction;
import hr.fer.oer.ga.model.Configuration;
import hr.fer.oer.ga.model.Pair;
import hr.fer.oer.ga.nodes.NonTerminalNode;
import hr.fer.oer.ga.nodes.TerminalVariableNode;
import hr.fer.oer.ga.nodes.ValueNode;
import hr.fer.oer.ga.tree.TreeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author matejc
 * Created on 16.11.2022.
 */

public class SymbolicRegressionAlgorithm implements IGeneticAlgorithm<ValueNode> {
    private final List<NonTerminalNode> nonTerminalNodes;
    private final List<TerminalVariableNode> variableNodes;
    private final ILossFunction meanSquareLossFunction;
    private final Configuration configuration;

    private final AtomicInteger costEvaluationCounter;

    private final Random random = new Random();

    public SymbolicRegressionAlgorithm(List<NonTerminalNode> nonTerminalNodes,
                                       List<TerminalVariableNode> variableNodes,
                                       ILossFunction lossFunction,
                                       Configuration configuration) {
        this.nonTerminalNodes = nonTerminalNodes;
        this.variableNodes = variableNodes;
        this.meanSquareLossFunction = lossFunction;
        this.configuration = configuration;

        costEvaluationCounter = new AtomicInteger(0);
    }

    @Override
    public ValueNode runGA(List<ValueNode> startingPopulation) {
        List<ValueNode> population = startingPopulation;
        costEvaluationCounter.set(0);

        ValueNode bestNode = bestNode(population).copyNode(null);
        for (int i = 0; i < configuration.generations() && costEvaluationCounter.get() < configuration.costEvaluations(); i++) {

            List<ValueNode> nextPopulation = new ArrayList<>();
            nextPopulation.add(bestNode);

            while (nextPopulation.size() < configuration.populationSize() - 1) {
                List<ValueNode> tournament = TreeUtils.pickNRandomElements(population, configuration.tournamentSize());
                var best = getBest2Nodes(tournament);

                double pick = random.nextDouble();

                ValueNode child1 = best.left();
                ValueNode child2 = best.right();

                if (pick < configuration.crossingProbability()) {
                    Pair<ValueNode> pair = TreeUtils.randomCrossNodes(best.left(), best.right(), configuration);
                    child1 = pair.left();
                    child2 = pair.right();
                } else if (random.nextDouble() < configuration.crossingProbability() + configuration.mutationProbability()) {
                    child1 = TreeUtils.mutateNode(child1, nonTerminalNodes, variableNodes, configuration);
                    child2 = TreeUtils.mutateNode(child2, nonTerminalNodes, variableNodes, configuration);
                } else {
                    child1 = child1.copyNode(null);
                    child2 = child2.copyNode(null);
                }

                nextPopulation.add(child1);
                nextPopulation.add(child2);
            }

            if (nextPopulation.size() == configuration.populationSize() - 1)
                nextPopulation.add(TreeUtils.getRandomElement(population));

            population = nextPopulation;
            bestNode = bestNode(population).copyNode(null);
            double loss = meanSquareLossFunction.lossAt(bestNode);
            System.out.printf("Generation: %d, loss = %.4f %n", i, loss);
            if (loss < 1E-7) break;
        }
        return bestNode;
    }

    private Pair<ValueNode> getBest2Nodes(List<ValueNode> nodes) {
        var list = nodes.parallelStream()
                .sorted(Comparator.comparing(
                        node -> {
                            costEvaluationCounter.getAndIncrement();
                            return meanSquareLossFunction.lossAt(node);
                        }))
                .limit(2)
                .toList();
        return new Pair<>(list.get(0), list.get(1));
    }

    private ValueNode bestNode(List<ValueNode> population) {
        return population.parallelStream()
                .min(Comparator.comparing(headNode -> {
                    costEvaluationCounter.getAndIncrement();
                    return meanSquareLossFunction.lossAt(headNode);
                }))
                .orElseThrow();
    }
}
