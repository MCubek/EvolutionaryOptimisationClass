package hr.fer.oer.ga.model;

import hr.fer.oer.ga.nodes.NonTerminalNode;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public record Configuration(
        Predicate<NonTerminalNode> functionPredicate,
        boolean constants,
        int minConstant,
        int maxConstant,
        int populationSize,
        int tournamentSize,
        int costEvaluations,
        double mutationProbability,
        int maxTreeDepth,
        boolean linearScaling,
        int halfHalfDepth,
        int generations,
        double crossingProbability,
        double copyProbability
) {
    public static Configuration fromProperties(Properties properties) {
        String functionNodes = properties.getProperty("FunctionNodes");
        String constantRange = properties.getProperty("ConstantRange");
        int populationSize = Integer.parseInt(properties.getProperty("PopulationSize"));
        int tournamentSize = Integer.parseInt(properties.getProperty("TournamentSize"));
        int costEvaluation = Integer.parseInt(properties.getProperty("CostEvaluations"));
        double mutationProbability = Double.parseDouble(properties.getProperty("MutationProbability"));
        int maxTreeDepth = Integer.parseInt(properties.getProperty("MaxTreeDepth"));
        int useLinearScaling = Integer.parseInt(properties.getProperty("UseLinearScaling"));
        int halfHalfDepth = Integer.parseInt(properties.getProperty("HalfHalfDepth"));
        int generations = Integer.parseInt(properties.getProperty("Generations"));
        double crossingProbability = Double.parseDouble(properties.getProperty("CrossingProbability"));
        double copyProbability = Double.parseDouble(properties.getProperty("CopyProbability"));

        List<String> functions = Arrays.stream(functionNodes.split(","))
                .map(String::strip)
                .toList();

        Predicate<NonTerminalNode> functionPredicate = x -> functions.contains(x.getNodeString());

        int min = 0;
        int max = 0;
        boolean constants = false;
        if (constantRange.contains(",")) {
            constants = true;
            String[] split = constantRange.split("\\s*,\\s*");
            min = Integer.parseInt(split[0].strip());
            max = Integer.parseInt(split[1].strip());
        }

        boolean linearScaling = useLinearScaling == 1;

        return new Configuration(functionPredicate,
                constants,
                min,
                max,
                populationSize,
                tournamentSize,
                costEvaluation,
                mutationProbability,
                maxTreeDepth,
                linearScaling,
                halfHalfDepth,
                generations,
                crossingProbability,
                copyProbability);
    }
}
