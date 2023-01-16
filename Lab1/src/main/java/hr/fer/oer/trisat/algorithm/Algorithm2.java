package hr.fer.oer.trisat.algorithm;

import hr.fer.oer.trisat.BitVector;
import hr.fer.oer.trisat.BitVectorNGenerator;
import hr.fer.oer.trisat.SATFormula;
import hr.fer.oer.trisat.SATFormulaStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Algorithm2 implements IOptAlgorithm {
    private final SATFormula formula;
    private final SATFormulaStats stats;

    private final Random random = new Random();

    private final int MAX_ITERATIONS = 10_000;

    public Algorithm2(SATFormula formula) {
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
            stats.setAssignment(bitVector, false);
            int fitness = stats.getNumberOfSatisfied();

            List<BitVector> neighbourhood = new ArrayList<>();

            for (BitVector neighbour : new BitVectorNGenerator(bitVector)) {
                stats.setAssignment(neighbour, false);
                if (stats.getNumberOfSatisfied() > fitness)
                    neighbourhood.add(neighbour);
            }

            if (neighbourhood.isEmpty()) {
                System.out.println("Local optimum. Quitting.");
                break;
            }

            bitVector = neighbourhood.get(random.nextInt(0, neighbourhood.size()));

        }
        return Optional.empty();
    }


}
