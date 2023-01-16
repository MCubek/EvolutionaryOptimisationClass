package hr.fer.oer.lab6;

import hr.fer.oer.lab6.algorithm.NSGA2;
import hr.fer.oer.lab6.crossover.ArithmeticCrossover;
import hr.fer.oer.lab6.crossover.ICrossOver;
import hr.fer.oer.lab6.mutation.GaussianMutation;
import hr.fer.oer.lab6.mutation.IMutation;
import hr.fer.oer.lab6.problem.MOOPProblem;
import hr.fer.oer.lab6.problem.Problem1;
import hr.fer.oer.lab6.problem.Problem2;
import hr.fer.oer.lab6.selection.CrowdedTournamentSelection;
import hr.fer.oer.lab6.selection.ISelection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author matejc
 * Created on 29.12.2022.
 */

public class MOOP {

    private static final String SOLUTIONS_FILE_PATH = "izlaz-dec.txt";
    private static final String OBJECTIVES_FILE_PATH = "izlaz-obj.txt";

    public static void main(String[] args) {
        if (args.length != 3) throw new IllegalArgumentException("Expecting 3 arguments");

        int functionSelector = Integer.parseInt(args[0]);
        int populationSize = Integer.parseInt(args[1]);
        int numberOfIterations = Integer.parseInt(args[2]);

        MOOPProblem problem = switch (functionSelector) {
            case 1 -> new Problem1();
            case 2 -> new Problem2();
            default -> throw new IllegalArgumentException("Problem id is invalid.");
        };

        IMutation mutation = new GaussianMutation(problem);
        ICrossOver crossOver = new ArithmeticCrossover(problem);
        ISelection selection = new CrowdedTournamentSelection();

        NSGA2 algorithm = new NSGA2(problem, populationSize, numberOfIterations, mutation, crossOver, selection);

        algorithm.run();

        Map<Integer, Integer> fronts = algorithm.getCountsPerFront();
        List<double[]> population = algorithm.getPopulation();
        List<double[]> objectives = population.stream()
                .map(solution -> problem.evaluateSolution(solution, null))
                .toList();

        System.out.println("Fronts: " + fronts.toString());

        try {
            writeToFile(SOLUTIONS_FILE_PATH + "-" + functionSelector, population);
            writeToFile(OBJECTIVES_FILE_PATH + "-" + functionSelector, objectives);
        } catch (IOException e) {
            System.err.println("Error while writing to file.");
            System.exit(-1);
        }
    }

    private static void writeToFile(String path, List<double[]> data) throws IOException {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(path))) {
            for (var line : data) {
                bufferedWriter.write(Arrays.stream(line)
                        .mapToObj(val -> String.format("%.5f", val))
                        .collect(Collectors.joining(" ", "(", ")")));
                bufferedWriter.write("\n");
            }
        }
    }
}
