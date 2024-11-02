package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import com.saicone.types.Types;
import com.saicone.types.iterator.ArrayIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Represents a parser that try to convert any object type to UUID.<br>
 * This parser is also compatible with 32-length string (without dashes),
 * 36-length string (with dashes), 2-length long array and 4-length int array.<br>
 * Take in count that any array type is accepted, and it's content
 * will be parsed using {@link Types#INTEGER} or {@link Types#LONG}.
 *
 * @author Rubenicos
 */
public class UuidParser implements TypeParser<UUID> {

    @Override
    public @Nullable Type getType() {
        return UUID.class;
    }

    @Override
    public @Nullable UUID parse(@NotNull Object object) {
        final Object single = IterableType.of(object).single();
        if (single == null) {
            return null;
        }

        if (single instanceof UUID) {
            return (UUID) single;
        } else if (single instanceof Object[] || single.getClass().isArray()) {
            return parseUuid(ArrayIterator.of(single));
        } else if (single instanceof String) {
            return parseUuid((String) single);
        }
        return null;
    }

    /**
     * Parses the given array iterator as UUID.<br>
     * Accepts 2-length long array representation of mostSigBits-leastSigBits,
     * and 4-length int array.
     *
     * @param iterator the iterator to parse.
     * @return         a converted UUID or null if the array is not valid.
     */
    @Nullable
    public UUID parseUuid(@NotNull ArrayIterator<?> iterator) {
        final int size = iterator.size();
        if (size == 2) {
            final Long mostSigBits = Types.LONG.parse(iterator.next());
            final Long leastSigBits = Types.LONG.parse(iterator.next());
            if (mostSigBits == null || leastSigBits == null) {
                return null;
            }
            return new UUID(mostSigBits, leastSigBits);
        } else if (size == 4) {
            StringBuilder builder = new StringBuilder(36);
            while (iterator.hasNext()) {
                final Integer i = Types.INTEGER.parse(iterator.next());
                if (i == null) {
                    return null;
                }
                String hex = Integer.toHexString(i);
                builder.append(new String(new char[8 - hex.length()]).replace('\0', '0')).append(hex);
            }
            if (builder.length() == 32) {
                builder.insert(20, '-').insert(16, '-').insert(12, '-').insert(8, '-');
                return UUID.fromString(builder.toString());
            } else {
                throw new IllegalArgumentException("The final converted UUID '" + builder + "' isn't a 32-length string");
            }
        }
        return null;
    }

    /**
     * Parses the given string as UUID.<br>
     * Accepts 36-length string (with dashes) and 32-length string (without dashes).
     *
     * @param s the string to parse.
     * @return  a converted UUID or null if the string is not valid.
     */
    @Nullable
    public UUID parseUuid(@NotNull String s) {
        if (s.length() == 36) {
            return UUID.fromString(s);
        } else if (s.length() == 32) {
            return UUID.fromString(new StringBuilder(s).insert(20, '-').insert(16, '-').insert(12, '-').insert(8, '-').toString());
        }
        return null;
    }
}
