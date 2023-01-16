package hr.fer.oer.trisat.algorithm;

import hr.fer.oer.trisat.BitVector;
import hr.fer.oer.trisat.MutableBitVector;
import hr.fer.oer.trisat.SATFormula;

import java.util.Optional;
import java.util.Random;

public class Algorithm6 implements IOptAlgorithm {
    private final SATFormula formula;

    private final Random random = new Random();

    private static final int MAX_ITERATIONS = 10_000;
    private static final double PERCENTAGE = 0.3;
    private final IOptAlgorithm localSearchAlgorithm;


    public Algorithm6(SATFormula formula) {
        this.formula = formula;

        this.localSearchAlgorithm = new Algorithm2(formula);
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {
        MutableBitVector bitVector = initial.map(BitVector::copy).orElse(new MutableBitVector(random, formula.getNumberOfVariables()));

        var result = localSearchAlgorithm.solve(Optional.of(bitVector));
        if (result.isPresent()) {
            return result;
        }

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            for (int j = 0; j < bitVector.getSize(); j++) {
                if (random.nextFloat() < PERCENTAGE) {
                    bitVector.switchBit(j);
                }
            }
            result = localSearchAlgorithm.solve(Optional.of(bitVector));
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }


}
