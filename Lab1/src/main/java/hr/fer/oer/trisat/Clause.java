package hr.fer.oer.trisat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Clause {

    private int[] indexes;

    public Clause(int[] indexes) {
        this.indexes = indexes.clone();
    }


    public int getSize() {
        return indexes.length;
    }


    public int getLiteral(int index) {
        return Math.abs(indexes[index]);
    }


    public boolean isSatisfied(BitVector assignment) {
        for (int index : indexes) {
            // Variables start at x1
            boolean bit = assignment.get(Math.abs(index) - 1);
            boolean positive = index > 0;

            if (bit && positive || ! bit && ! positive) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.stream(indexes)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ", "(", ")"));
    }
}
