package hr.fer.oer.lab6.algorithm;

import hr.fer.oer.lab6.problem.MOOPProblem;

import java.util.*;

/**
 * @author matejc
 * Created on 30.12.2022.
 */

public class PopulationEvaluation {

    private final MOOPProblem problem;

    private final Map<double[], double[]> objectives;
    private final Map<Integer, List<double[]>> fronts;
    private final Map<double[], Integer> solutionFrontMap;


    private PopulationEvaluation(MOOPProblem problem, Map<double[], double[]> objectives, Map<Integer, List<double[]>> fronts, Map<double[], Integer> solutionFrontMap) {
        this.problem = problem;
        this.objectives = objectives;
        this.fronts = fronts;
        this.solutionFrontMap = solutionFrontMap;
    }

    public static PopulationEvaluation evaluate(MOOPProblem problem, List<double[]> population) {
        Map<double[], double[]> objectives = new HashMap<>();
        for (var solution : population) {
            objectives.put(solution, problem.evaluateSolution(solution, null));
        }

        Map<Integer, List<double[]>> fronts = nonDominatedSorting(problem, population);
        Map<double[], Integer> solutionFrontMap = mapFronts(fronts);

        return new PopulationEvaluation(problem, objectives, fronts, solutionFrontMap);
    }

    private static Map<double[], Integer> mapFronts(Map<Integer, List<double[]>> fronts) {
        Map<double[], Integer> map = new HashMap<>();

        for (var entry : fronts.entrySet()) {
            entry.getValue()
                    .forEach(solution -> map.put(solution, entry.getKey()));
        }

        return map;
    }

    private static Map<Integer, List<double[]>> nonDominatedSorting(MOOPProblem problem, List<double[]> population) {
        Map<Integer, List<double[]>> fronts = new LinkedHashMap<>();

        Map<double[], List<double[]>> dominatingSolutions = new HashMap<>();
        Map<double[], Integer> dominatedByNumber = new HashMap<>();

        population.forEach(solution -> dominatedByNumber.put(solution, 0));


        for (int j = 0; j < population.size(); j++) {
            double[] solution1 = population.get(j);

            List<double[]> currentDominatingList = new ArrayList<>();

            for (int i = 0; i < population.size(); i++) {
                if (i == j) continue;
                double[] solution2 = population.get(i);

                if (firstSolutionDominatesSecond(problem, solution1, solution2)) {
                    currentDominatingList.add(solution2);
                    dominatedByNumber.computeIfPresent(solution2, (k, vOld) -> vOld + 1);
                }
            }

            dominatingSolutions.put(solution1, currentDominatingList);

        }
        dominatedByNumber.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .forEach(solution -> fronts.computeIfAbsent(1, v -> new ArrayList<>()).add(solution));

        int currentFront = 1;
        List<double[]> populationFront = fronts.computeIfAbsent(currentFront, v -> new ArrayList<>());

        while (!populationFront.isEmpty()) {
            Set<double[]> nextFrontQ = new HashSet<>();

            for (double[] solutionInFront : populationFront) {

                for (double[] dominatedSolution : dominatingSolutions.get(solutionInFront)) {
                    int newDominatedByNumber = Objects.requireNonNull(
                            dominatedByNumber.computeIfPresent(dominatedSolution, (k, v) -> v - 1));

                    if (newDominatedByNumber == 0) {
                        nextFrontQ.add(dominatedSolution);
                    }
                }

            }
            currentFront += 1;

            var frontList = new ArrayList<>(nextFrontQ);

            fronts.put(currentFront, frontList);
            populationFront = frontList;
        }
        return fronts;
    }

    private static boolean firstSolutionDominatesSecond(MOOPProblem problem, double[] solution1, double[] solution2) {
        boolean dominates = false;
        for (int i = 0; i < solution1.length; i++) {
            double x = problem.evaluateSolution(solution1, null)[i];
            double y = problem.evaluateSolution(solution2, null)[i];

            if (y < x) return false;

            if (x < y) dominates = true;
        }
        return dominates;
    }


    public int getRank(double[] solution) {
        if (!solutionFrontMap.containsKey(solution)) throw new IllegalStateException("Solution not found in any front");
        return solutionFrontMap.get(solution);
    }

    public double getDistance(double[] solution) {
        var frontNumber = solutionFrontMap.get(solution);
        var front = fronts.get(frontNumber);

        int numberOfSolutionsInFront = front.size();
        double[] distances = new double[numberOfSolutionsInFront];

        Map<double[], Integer> valueIndexMap = new HashMap<>();
        for (int i = 0; i < front.size(); i++) {
            double[] sol = front.get(i);
            valueIndexMap.put(sol, i);
        }

        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            int finalI = i;

            front.sort((o1, o2) -> {
                double value1 = objectives.get(o1)[finalI];
                double value2 = objectives.get(o2)[finalI];

                return Double.compare(value1, value2);
            });

            int minIndex = valueIndexMap.get(front.get(0));
            int maxIndex = valueIndexMap.get(front.get(front.size() - 1));
            distances[minIndex] = Double.MAX_VALUE;
            distances[maxIndex] = Double.MAX_VALUE;

            double fMin = objectives.get(front.get(0))[finalI];
            double fMax = objectives.get(front.get(front.size() - 1))[finalI];

            for (int j = 1; j < front.size() - 1; j++) {
                int solutionIndex = valueIndexMap.get(front.get(j));

                double fPrev = objectives.get(front.get(j - 1))[finalI];
                double fNext = objectives.get(front.get(j + 1))[finalI];

                distances[solutionIndex] += (fNext - fPrev) / (fMax - fMin);
            }

        }

        return distances[valueIndexMap.get(solution)];
    }

    public Map<Integer, List<double[]>> getFronts() {
        return fronts;
    }

    public static Comparator<double[]> getCrowdingDistanceComparator(PopulationEvaluation evaluation) {
        return Comparator.comparingDouble(evaluation::getDistance);
    }
}
