package hr.fer.oer.ga.function;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

@FunctionalInterface
public interface IBiNonTerminalFunction<T> {
    T apply(T first, T second);
}
