package hr.fer.oer.ga;

import hr.fer.oer.ga.algorithm.IGeneticAlgorithm;
import hr.fer.oer.ga.algorithm.SymbolicRegressionAlgorithm;
import hr.fer.oer.ga.factory.NodeFactory;
import hr.fer.oer.ga.function.ILossFunction;
import hr.fer.oer.ga.function.MeanSquareLossFunction;
import hr.fer.oer.ga.function.ScaledMeanSquareLossFunction;
import hr.fer.oer.ga.model.Configuration;
import hr.fer.oer.ga.model.Reading;
import hr.fer.oer.ga.nodes.ValueNode;
import hr.fer.oer.ga.tree.TreeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * @author matejc
 * Created on 15.11.2022.
 */

public class GARunner {

    private static final Path configFile = Path.of("src/main/resources/application.properties");

    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Required path to function readings.");

        Path data = Path.of(args[0]);
        if (! Files.isReadable(data)) throw new IllegalArgumentException("Function data can't be read.");
        if (! Files.isReadable(configFile)) throw new IllegalArgumentException("Config file can't be read.");


        try (InputStream is = Files.newInputStream(configFile)) {
            Properties properties = new Properties();
            properties.load(is);

            Configuration configuration = Configuration.fromProperties(properties);

            List<Reading> readings = loadReadingsFromFile(data);

            var nonTerminalNodes = NodeFactory.getFunctions()
                    .stream()
                    .filter(configuration.functionPredicate())
                    .toList();

            int variables = readings.get(0).variableNumber();
            var variableNodes = NodeFactory.createVariableNodes(variables);

            ILossFunction lossFunction;
            if (configuration.linearScaling()) {
                lossFunction = new ScaledMeanSquareLossFunction(readings);
            } else {
                lossFunction = new MeanSquareLossFunction(readings);
            }
            IGeneticAlgorithm<ValueNode> algorithm = new SymbolicRegressionAlgorithm(nonTerminalNodes, variableNodes, lossFunction, configuration);

            List<ValueNode> initialPopulation = TreeUtils.createRampedHalfAndHalfPopulation(configuration, configuration.halfHalfDepth(), nonTerminalNodes, variableNodes);

            ValueNode solution = algorithm.runGA(initialPopulation);

            System.out.println(lossFunction.getStringWithModifiers(solution));
            System.out.printf("Loss = %.4f%n", lossFunction.lossAt(solution));

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(- 1);
        }

    }

    private static List<Reading> loadReadingsFromFile(Path data) throws IOException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(data)) {
            return bufferedReader.lines()
                    .map(Reading::parseReading)
                    .toList();
        }
    }
}
