package hr.fer.oer.trisat.algorithm;

import hr.fer.oer.trisat.BitVector;

import java.util.Optional;

public interface IOptAlgorithm {
    Optional<BitVector> solve(Optional<BitVector> initial);
}
