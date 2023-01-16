package hr.fer.oer.ga.function;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

@FunctionalInterface
public interface INonTerminalFunction<T> {
    T apply(T element);
}
