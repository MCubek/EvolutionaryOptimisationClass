package hr.fer.oer.trisat;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BitVector {

    protected boolean[] bits;


    public BitVector(Random rand, int numberOfBits) {
        this.bits = new boolean[numberOfBits];

        for (int i = 0; i < numberOfBits; i++) {
            this.bits[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean... bits) {
        this.bits = bits.clone();
    }

    public BitVector(int n) {
        this.bits = new boolean[n];
    }

    public boolean get(int index) {
        return bits[index];
    }

    public int getSize() {
        return bits.length;
    }

    @Override
    public String toString() {
        return IntStream.range(0, bits.length)
                .mapToObj(id -> bits[id])
                .map(x -> Boolean.TRUE.equals(x) ? 1 : 0)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public MutableBitVector copy() {
        return new MutableBitVector(bits);
    }
}
