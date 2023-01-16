package hr.fer.oer.trisat.algorithm;

import hr.fer.oer.trisat.BitVector;
import hr.fer.oer.trisat.BitVectorNGenerator;
import hr.fer.oer.trisat.SATFormula;
import hr.fer.oer.trisat.SATFormulaStats;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Algorithm3 implements IOptAlgorithm {
    private final SATFormula formula;
    private final SATFormulaStats stats;

    private final Random random = new Random();

    private static final int MAX_ITERATIONS = 100000;
    private static final int NUMBER_OF_BEST = 2;

    public Algorithm3(SATFormula formula) {
        this.formula = formula;
        this.stats = new SATFormulaStats(formula);
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {

        BitVector bitVector = initial.orElse(new BitVector(random, formula.getNumberOfVariables()));

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (formula.isSatisfied(bitVector)) {
                return Optional.of(bitVector);
            }
            stats.setAssignment(bitVector, true);

            Map<BitVector, Double> neighbourhood = new HashMap<>();

            for (BitVector neighbour : new BitVectorNGenerator(bitVector)) {
                stats.setAssignment(neighbour, true);
                neighbourhood.put(neighbour, stats.getNumberOfSatisfied() + stats.getPercentageBonus());
            }

            var best = neighbourhood.entrySet().stream()
                    .sorted(Map.Entry.<BitVector, Double>comparingByValue().reversed())
                    .limit(NUMBER_OF_BEST)
                    .map(Map.Entry::getKey)
                    .toList();


            bitVector = best.get(random.nextInt(0, best.size()));

        }
        return Optional.empty();
    }


}
