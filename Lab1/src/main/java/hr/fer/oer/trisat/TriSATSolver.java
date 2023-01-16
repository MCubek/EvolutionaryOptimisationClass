package hr.fer.oer.trisat;

import hr.fer.oer.trisat.algorithm.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TriSATSolver {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException("2 Arguments required");

        int algorithmId = Integer.parseInt(args[0]);
        Path filePath = Path.of(args[1]);


        SATFormula formula = loadFormulaFromFile(filePath);
        IOptAlgorithm alg = switch (algorithmId) {
            case 1 -> new Algorithm1(formula);
            case 2 -> new Algorithm2(formula);
            case 3 -> new Algorithm3(formula);
            case 4 -> new Algorithm4(formula);
            case 5 -> new Algorithm5(formula);
            case 6 -> new Algorithm6(formula);
            default -> throw new IllegalStateException("Unexpected algorithm id: " + algorithmId);
        };

        Optional<BitVector> solution = alg.solve(Optional.empty());

        if (solution.isPresent()) {
            BitVector sol = solution.get();
            System.out.println("Solution found: " + sol);
        } else {

            System.out.println("Solution not found.");
        }
    }

    private static SATFormula loadFormulaFromFile(Path filePath) throws IOException {

        List<String> lines = Files.readAllLines(filePath);

        int varsNum = 0;
        int clausesNum = 0;
        ArrayList<Clause> clauses = new ArrayList<>();

        for (var line : lines) {
            if (line.startsWith("c")) continue;
            if (line.startsWith("%")) break;

            var split = line.strip().split("\\s+");
            if (line.startsWith("p")) {
                varsNum = Integer.parseInt(split[2]);
                clausesNum = Integer.parseInt(split[3]);
            } else {
                var indexes = Arrays.stream(split)
                        .mapToInt(Integer::parseInt)
                        .filter(x -> x != 0)
                        .toArray();

                clauses.add(new Clause(indexes));
            }
        }
        if (clauses.size() != clausesNum)
            throw new IllegalArgumentException("Wrong number of clauses! %d but expected %d."
                    .formatted(clauses.size(), clausesNum));

        return new SATFormula(varsNum, clauses.toArray(new Clause[0]));
    }
}
