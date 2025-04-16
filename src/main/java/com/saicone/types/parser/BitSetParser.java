package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.BitSet;

/**
 * Represents a parser that try to convert any number representation of bitset.
 *
 * @author Rubenicos
 */
public class BitSetParser extends AllocParser<BitSet> {

    /**
     * {@link BitSetParser} public instance.
     */
    public static final BitSetParser INSTANCE = new BitSetParser();

    /**
     * Construct a BitSet parser.
     */
    public BitSetParser() {
        super(capacity -> capacity == null ? new BitSet() : new BitSet(capacity));
    }

    @Override
    public @Nullable Type getType() {
        return BitSet.class;
    }

    @Override
    public @NotNull BitSet parse(@Nullable Object object) {
        if (object == null) {
            return create();
        } else if (isInstance(object)) {
            return (BitSet) object;
        }
        final AnyIterable<Object> iterable = AnyIterable.of(object);
        if (!iterable.isIterable()) {
            final BitSet bitSet = create();

            final Integer value = Types.INTEGER.parse(object);
            if (value != null) {
                for (int bitIndex = 0; bitIndex < 32; bitIndex++) {
                    if ((value & (1 << bitIndex)) != 0) {
                        bitSet.set(bitIndex);
                    }
                }
            }

            return bitSet;
        }

        final int size = iterable.size(); // Initial capacity
        final BitSet bitSet = create(size);

        int bitIndex = 0;
        for (Object element : iterable) {
            if (element instanceof Boolean) {
                bitSet.set(bitIndex, (Boolean) element);
            } else if (element instanceof String && BooleanParser.isValid((String) element)) {
                bitSet.set(bitIndex, Types.BOOLEAN.parse(element));
            } else {
                final Integer index = Types.INTEGER.parse(element);
                if (index != null) {
                    bitSet.set(index);
                }
            }
            bitIndex++;
        }

        return bitSet;
    }
}
