package hr.fer.zemirs.oer.dz3.algorithm.simulated_annaeling.temperature;

/**
 * @author matejc
 * Created on 04.11.2022.
 */

public interface ITemperatureStrategy {
    void initialiseCurrentTemperature();

    double getAndUpdateTemperature();

    int innerLoopCount();

    double minTemperature();
}
