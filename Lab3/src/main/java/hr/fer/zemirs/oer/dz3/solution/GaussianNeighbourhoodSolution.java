package hr.fer.zemirs.oer.dz3.solution;

import org.apache.commons.math3.linear.RealVector;

import java.util.Random;

/**
 * @author matejc
 * Created on 04.11.2022.
 */

public class GaussianNeighbourhoodSolution extends Solution {
    private final Random random;
    private final double standardDeviation;

    public GaussianNeighbourhoodSolution(RealVector proxy, Random random, double sttdev) {
        super(proxy);
        this.random = random;
        this.standardDeviation = sttdev;
    }

    @Override
    public Solution getRandomNeighbour() {
//        RealVector neighbour = proxy.map(el -> random.nextBoolean() ? el + random.nextGaussian(0, standardDeviation) : el);
        RealVector neighbour = proxy.copy();

        int index = random.nextInt(neighbour.getDimension());
        double oldValue = neighbour.getEntry(index);
        double newValue = oldValue + random.nextGaussian(0, standardDeviation);

        if (Math.abs(newValue) > 20) newValue = oldValue;

        neighbour.setEntry(index, newValue);

        return new GaussianNeighbourhoodSolution(neighbour, random, standardDeviation);
    }
}
