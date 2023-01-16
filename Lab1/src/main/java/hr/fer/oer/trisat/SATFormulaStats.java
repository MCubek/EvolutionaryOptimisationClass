package hr.fer.oer.trisat;

import java.util.Arrays;

public class SATFormulaStats {

    private final SATFormula satFormula;
    private BitVector assignment;
    private double[] post;
    private double PERCENTAGE_CONSTANT_UP = 0.01;
    private double PERCENTAGE_CONSTANT_DOWN = 0.1;
    private double PERCENTAGE_UNIT_AMOUNT = 50;


    public SATFormulaStats(SATFormula formula) {
        this.satFormula = formula;

        reset();
    }

    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        this.assignment = assignment;

        if (updatePercentages) updateStats();
    }

    private void updateStats() {
        for (int i = 0; i < satFormula.getNumberOfClauses(); i++) {
            Clause clause = satFormula.getClause(i);

            if (clause.isSatisfied(assignment))
                post[i] += ((1 - post[i]) * PERCENTAGE_CONSTANT_UP);
            else
                post[i] += ((0 - post[i]) * PERCENTAGE_CONSTANT_DOWN);
        }
    }

    public int getNumberOfSatisfied() {
        return (int) Arrays.stream(satFormula.getClauses())
                .map(clause -> clause.isSatisfied(assignment))
                .filter(Boolean.TRUE::equals)
                .count();
    }


    public boolean isSatisfied() {
        return satFormula.isSatisfied(assignment);
    }


    public double getPercentageBonus() {
        double bonus = 0;

        for (int i = 0; i < satFormula.getNumberOfClauses(); i++) {
            Clause clause = satFormula.getClause(i);

            if (clause.isSatisfied(assignment))
                bonus += PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
            else
                bonus -= PERCENTAGE_UNIT_AMOUNT * (1 - post[i]);
        }

        return bonus;
    }


    public double getPercentage(int index) {
        return post[index];
    }

    public void reset() {
        post = new double[satFormula.getNumberOfClauses()];
    }
}
