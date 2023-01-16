package hr.fer.oer.lab5.algorithm;

import hr.fer.oer.lab5.function.IFunction;
import hr.fer.oer.lab5.util.PointUtil;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public abstract class ParticleSwarmAlgorithm implements IAlgorithm {
    protected final Random random = new Random();

    protected final IFunction function;
    private final int variables;
    protected final int populationSize;
    protected final int iterations;


    protected final double[] f;
    protected final double[] p_best_f;
    protected final RealVector[] p_best;
    protected double g_best_f;
    protected RealVector g_best;

    private static final double POINT_RANGE_LOWER = -10;
    private static final double POINT_RANGE_HIGHER = 10;
    protected static final double VELOCITY_RANGE_LOWER = -2;
    protected static final double VELOCITY_RANGE_HIGHER = 2;

    protected static final double C1 = 2.0;
    protected static final double C2 = 2.0;

    private static final double ZERO_ERROR = 1E-5;


    protected List<RealVector> positions;
    protected List<RealVector> velocities;

    protected ParticleSwarmAlgorithm(IFunction function, int populationSize, int iterations) {
        this.function = function;
        this.variables = function.numberOfVariables();
        this.populationSize = populationSize;
        this.iterations = iterations;

        this.positions = initialisePopulationPositions();
        this.velocities = initialisePopulationVelocities();

        this.f = new double[populationSize];
        this.p_best_f = new double[populationSize];
        Arrays.fill(p_best_f, Double.MAX_VALUE);
        this.p_best = new RealVector[populationSize];

        this.g_best_f = Double.MAX_VALUE;
        this.g_best = null;
    }

    private List<RealVector> initialisePopulationPositions() {
        List<RealVector> positionList = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            positionList.add(PointUtil.randomVector(POINT_RANGE_LOWER, POINT_RANGE_HIGHER, variables));
        }
        return positionList;
    }

    private List<RealVector> initialisePopulationVelocities() {
        List<RealVector> velocityList = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            velocityList.add(PointUtil.randomVector(VELOCITY_RANGE_LOWER, VELOCITY_RANGE_HIGHER, variables));
        }
        return velocityList;
    }


    @Override
    public RealVector run() {
        for (int iter = 0; iter < iterations; iter++) {

            for (int particleIndex = 0; particleIndex < populationSize; particleIndex++) {
                RealVector position = positions.get(particleIndex);

                double value = function.errorAt(position);
                f[particleIndex] = value;

                if (value < p_best_f[particleIndex]) {
                    p_best_f[particleIndex] = value;
                    p_best[particleIndex] = position;
                }

                if (value < g_best_f) {
                    g_best_f = value;
                    g_best = position;
                }

            }

            if (iter % 100 == 0)
                System.out.printf("Iteration %d, error: %.4f%n", iter, g_best_f);

            if (g_best_f < ZERO_ERROR) {
                System.out.println("Zero error reached.");
                break;
            }
            updatePopulation();
        }

        return g_best;
    }

    protected abstract void updatePopulation();
}
