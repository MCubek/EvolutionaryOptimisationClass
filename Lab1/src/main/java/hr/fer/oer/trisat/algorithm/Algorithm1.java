package hr.fer.oer.trisat.algorithm;

import hr.fer.oer.trisat.BitVector;
import hr.fer.oer.trisat.MutableBitVector;
import hr.fer.oer.trisat.SATFormula;

import java.util.Optional;

public class Algorithm1 implements IOptAlgorithm {
    private final SATFormula formula;


    public Algorithm1(SATFormula formula) {
        this.formula = formula;
    }

    @Override
    public Optional<BitVector> solve(Optional<BitVector> initial) {

        MutableBitVector bitVector = initial.map(BitVector::copy)
                .orElse(new MutableBitVector(formula.getNumberOfVariables()));

        for (int i = 0; i < Math.pow(2, formula.getNumberOfVariables()); i++) {
            if (formula.isSatisfied(bitVector)) {
                return Optional.of(bitVector);
            }
            bitVector.incrementByOne();
        }
        return Optional.empty();
    }
}
