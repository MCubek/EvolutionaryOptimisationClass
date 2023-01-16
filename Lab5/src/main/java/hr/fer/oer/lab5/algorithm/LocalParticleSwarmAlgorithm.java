package hr.fer.oer.lab5.algorithm;

import hr.fer.oer.lab5.function.IFunction;
import org.apache.commons.math3.linear.RealVector;

import java.util.Comparator;
import java.util.stream.IntStream;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class LocalParticleSwarmAlgorithm extends ParticleSwarmAlgorithm {
    private static final int NS = 3;

    public LocalParticleSwarmAlgorithm(IFunction function, int populationSize, int iterations) {
        super(function, populationSize, iterations);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void updatePopulation() {
        for (int particleIndex = 0; particleIndex < populationSize; particleIndex++) {
            RealVector position = positions.get(particleIndex);
            RealVector velocity = velocities.get(particleIndex);

            RealVector personalBest = p_best[particleIndex];
            RealVector bestNeighbour = findBestNeighbour(particleIndex);

            var a = personalBest.subtract(position).mapMultiplyToSelf(C1 * random.nextDouble());
            var b = bestNeighbour.subtract(position).mapMultiplyToSelf(C2 * random.nextDouble());

            var ab = a.add(b);

            velocity = velocity.add(ab);

            velocity.mapToSelf(val -> {
                if (val < VELOCITY_RANGE_LOWER) return VELOCITY_RANGE_LOWER;
                else if (val > VELOCITY_RANGE_HIGHER) return VELOCITY_RANGE_HIGHER;
                return val;
            });
            velocities.set(particleIndex, velocity);

            position = position.add(velocity);
            positions.set(particleIndex, position);
        }
    }

    private RealVector findBestNeighbour(int particleIndex) {
        return IntStream.range(particleIndex - NS + 1, particleIndex + NS)
                .filter(index -> index >= 0 && index < populationSize)
                .mapToObj(positions::get)
                .min(Comparator.comparing(function::errorAt))
                .orElseThrow();
    }
}
