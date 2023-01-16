package hr.fer.oer.lab5.algorithm;

import hr.fer.oer.lab5.function.IFunction;
import org.apache.commons.math3.linear.RealVector;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class GlobalParticleSwarmAlgorithm extends ParticleSwarmAlgorithm {
    public GlobalParticleSwarmAlgorithm(IFunction function, int populationSize, int iterations) {
        super(function, populationSize, iterations);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void updatePopulation() {
        for (int particleIndex = 0; particleIndex < populationSize; particleIndex++) {
            RealVector position = positions.get(particleIndex);
            RealVector velocity = velocities.get(particleIndex);

            RealVector personalBest = p_best[particleIndex];
            RealVector globalBest = g_best;

            var a = personalBest.subtract(position).mapMultiplyToSelf(C1 * random.nextDouble());
            var b = globalBest.subtract(position).mapMultiplyToSelf(C2 * random.nextDouble());

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
}
