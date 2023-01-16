package hr.fer.oer.lab6.mutation;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

@FunctionalInterface
public interface IMutation {
    void mutate(double[] solution);
}
