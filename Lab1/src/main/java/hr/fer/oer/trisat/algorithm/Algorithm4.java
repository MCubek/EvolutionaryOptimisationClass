package hr.fer.oer.trisat.algorithm;

import hr.fer.oer.trisat.BitVector;
import hr.fer.oer.trisat.BitVectorNGenerator;
import hr.fer.oer.trisat.SATFormula;
import hr.fer.oer.trisat.SATFormulaStats;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Algorithm4 implements IOptAlgorithm {
    private final SATFormula formula;
    private final SATFormulaStats stats;

    private final Random random = new Random();

    private static final int MAX_FLIPS = 10_000;
    private static final int MAX_TRIES = 1000;

    public Algorithm4(SATFormula formula) {
        this.formula = formula;
        this.stats = new SATFormulaStats(formula);
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {

        for (int i = 0; i < MAX_TRIES; i++) {
            BitVector bitVector = initial.orElse(new BitVector(random, formula.getNumberOfVariables()));
            initial = Optional.empty();

            for (int j = 0; j < MAX_FLIPS; j++) {

                if (formula.isSatisfied(bitVector)) {
                    return Optional.of(bitVector);
                }

                Map<BitVector, Integer> neighbourhood = new HashMap<>();

                for (BitVector neighbour : new BitVectorNGenerator(bitVector)) {
                    stats.setAssignment(neighbour, false);
                    neighbourhood.put(neighbour, stats.getNumberOfSatisfied());
                }

                int bestStats = neighbourhood.values().stream()
                        .mapToInt(x -> x)
                        .max().orElse(0);

                var bestNeighbours = neighbourhood.entrySet().stream()
                        .filter(entry -> entry.getValue() == bestStats)
                        .map(Map.Entry::getKey)
                        .toList();

                bitVector = bestNeighbours.get(random.nextInt(0, bestNeighbours.size()));

            }

        }
        return Optional.empty();
    }


}
