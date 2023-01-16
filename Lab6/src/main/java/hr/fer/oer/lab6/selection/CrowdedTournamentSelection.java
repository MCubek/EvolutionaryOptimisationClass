package hr.fer.oer.lab6.selection;

import hr.fer.oer.lab6.algorithm.PopulationEvaluation;

import java.util.List;
import java.util.Random;

/**
 * @author matejc
 * Created on 30.12.2022.
 */

public class CrowdedTournamentSelection implements ISelection {

    private static final int TOURNAMENT_SIZE = 7;

    private static final Random random = new Random();

    @Override
    public double[] select(List<double[]> population, PopulationEvaluation populationEvaluation) {
        int populationSize = population.size();

        double[] selection = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            double[] pick = population.get(random.nextInt(populationSize));

            if (selection == null) selection = pick;

            int pickRank = populationEvaluation.getRank(pick);
            int selectionRank = populationEvaluation.getRank(selection);

            if (pickRank < selectionRank ||
                    (pickRank == selectionRank &&
                            populationEvaluation.getDistance(pick) > populationEvaluation.getDistance(selection)))
                selection = pick;
        }

        return selection;
    }
}
