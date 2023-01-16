package hr.fer.oer.trisat;

import java.util.ArrayList;
import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {

    private final BitVector bitVector;

    public BitVectorNGenerator(BitVector bitVector) {
        this.bitVector = bitVector;
    }

    @Override
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<>() {

            int i = 0;

            @Override
            public boolean hasNext() {
                return i < bitVector.getSize();
            }

            @Override
            public MutableBitVector next() {
                if (! hasNext()) throw new IndexOutOfBoundsException();

                var copy = bitVector.copy();

                copy.switchBit(i++);

                return copy;
            }
        };
    }

    public MutableBitVector[] createNeighborhood() {
        ArrayList<MutableBitVector> neighborhood = new ArrayList<>();
        iterator().forEachRemaining(neighborhood::add);

        return neighborhood.toArray(new MutableBitVector[0]);
    }
}
