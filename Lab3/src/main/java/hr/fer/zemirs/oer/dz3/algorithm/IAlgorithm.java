package hr.fer.zemirs.oer.dz3.algorithm;

import java.util.Optional;

/**
 * @author matejc
 * Created on 04.11.2022.
 */

public interface IAlgorithm<T> {
    T run(T startingPoint);
}
