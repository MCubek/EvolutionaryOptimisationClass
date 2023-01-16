package hr.fer.oer.trisat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SATFormula {

    private final int numberOfVariables;
    private final Clause[] clauses;

    public SATFormula(int numberOfVariables, Clause[] clauses) {
        this.numberOfVariables = numberOfVariables;
        this.clauses = clauses;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfClauses() {
        return clauses.length;
    }

    public Clause getClause(int index) {
        return clauses[index];
    }

    public Clause[] getClauses() {
        return clauses;
    }

    public boolean isSatisfied(BitVector assignment) {
        for (Clause clause : clauses) {
            if (! clause.isSatisfied(assignment)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.stream(clauses)
                .map(Clause::toString)
                .collect(Collectors.joining(" "));
    }
}
