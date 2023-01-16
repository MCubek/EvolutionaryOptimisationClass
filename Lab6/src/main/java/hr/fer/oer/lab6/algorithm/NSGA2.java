package hr.fer.oer.lab6.algorithm;

import hr.fer.oer.lab6.crossover.ICrossOver;
import hr.fer.oer.lab6.mutation.IMutation;
import hr.fer.oer.lab6.problem.MOOPProblem;
import hr.fer.oer.lab6.selection.ISelection;

import java.util.*;
import java.util.stream.Collectors;

import static hr.fer.oer.lab6.algorithm.PopulationEvaluation.evaluate;

/**
 * @author matejc
 * Created on 30.12.2022.
 */

public class NSGA2 {
    private static final Random random = new Random();
    private final MOOPProblem problem;
    private final int populationSize;
    private final int numberOfIterations;
    private final IMutation mutation;
    private final ICrossOver crossOver;
    private final ISelection selection;
    private List<double[]> population;

    public NSGA2(MOOPProblem problem, int populationSize, int numberOfIterations, IMutation mutation, ICrossOver crossOver, ISelection selection) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.numberOfIterations = numberOfIterations;
        this.mutation = mutation;
        this.crossOver = crossOver;
        this.selection = selection;

        this.population = new ArrayList<>(populationSize);
    }

    public void run() {
        initializePopulation();

        for (int i = 0; i < numberOfIterations; i++) {
            var populationEvaluation = evaluate(problem, population);

            if (i % 10 == 0) {
                double[] best = populationEvaluation.getFronts().get(1).get(0);
                String outputString = Arrays.stream(problem.evaluateSolution(best, null))
                        .mapToObj("%.5f"::formatted)
                        .collect(Collectors.joining(" "));

                System.out.printf("Iteration: %d, function outputs: %s%n", i, outputString);
            }

            List<double[]> childPopulation = createChildPopulation(population, populationEvaluation);

            population = selectNextGeneration(population, childPopulation);
        }
    }

    private List<double[]> createChildPopulation(List<double[]> population, PopulationEvaluation populationEvaluation) {
        List<double[]> childPopulation = new ArrayList<>();

        while (childPopulation.size() < populationSize) {
            var parent1 = selection.select(population, populationEvaluation);
            var parent2 = selection.select(population, populationEvaluation);

            var child = crossOver.cross(parent1, parent2);
            mutation.mutate(child);

            childPopulation.add(child);
        }

        return childPopulation;
    }

    private List<double[]> selectNextGeneration(List<double[]> population, List<double[]> childPopulation) {
        List<double[]> joined = new ArrayList<>(population);
        joined.addAll(childPopulation);

        var evaluation = evaluate(problem, joined);
        Map<Integer, List<double[]>> fronts = evaluation.getFronts();

        List<double[]> nextGeneration = new ArrayList<>();
        int currentFront = 1;
        while (nextGeneration.size() + fronts.get(currentFront).size() <= populationSize) {
            nextGeneration.addAll(fronts.get(currentFront));
            currentFront++;
        }

        if (nextGeneration.size() < populationSize) {
            List<double[]> nextFront = fronts.get(currentFront);

            Comparator<double[]> crowdingDistanceComparator = PopulationEvaluation.getCrowdingDistanceComparator(evaluation);

            var sortedFront = new ArrayList<>(nextFront);
            sortedFront.sort(crowdingDistanceComparator);

            for (var solution : sortedFront) {
                nextGeneration.add(solution);

                if (nextGeneration.size() == populationSize) break;
            }
        }
        return nextGeneration;
    }

    private void initializePopulation() {
        var minValues = problem.getMinValues();
        var maxValues = problem.getMaxValues();

        for (int i = 0; i < populationSize; i++) {
            double[] sample = new double[problem.getNumberOfVariables()];

            for (int j = 0; j < sample.length; j++) {
                sample[j] = random.nextDouble(minValues[j], maxValues[j]);
            }
            population.add(sample);
        }
    }

    public Map<Integer, Integer> getCountsPerFront() {
        var fronts = evaluate(problem, population).getFronts();

        return fronts.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }

    public List<double[]> getPopulation() {
        return population;
    }
}
