package hr.fer.oer.trisat;

import java.util.Random;

public class MutableBitVector extends BitVector {
    public MutableBitVector(boolean... bits) {
        super(bits);
    }

    public MutableBitVector(int n) {
        super(n);
    }

    public MutableBitVector(Random random, int n) {
        super(random, n);
    }

    public void set(int index, boolean value) {
        bits[index] = value;
    }

    public void switchBit(int index) {
        bits[index] = ! bits[index];
    }

    /**
     * Method increments bitvector by one.
     * From the beginning to the end it turns 1 into 0 and when it gets
     * to a 0 it makes it into 1 and returns.
     */
    public void incrementByOne() {
        for (int i = 0; i < getSize(); i++) {
            var value = get(i);
            switchBit(i);
            if (! value) return;
        }
    }
}
