package com.saicone.types.parser;

import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a parser that convert objects into an allocatable size object.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the allocatable size structure.
 */
public abstract class AllocParser<T> implements TypeParser<T> {

    private final Function<Integer, T> supplier;

    /**
     * Construct an allocatable size object parser with function that supply a newly generated object to fill.
     *
     * @param supplier the function that generate an object with allocatable size.
     */
    public AllocParser(@NotNull Function<Integer, T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Create an allocatable size object without a defined capacity.
     *
     * @return a newly generated structure.
     */
    @NotNull
    public T create() {
        return supplier.apply(null);
    }

    /**
     * Create an allocatable size object with a defined capacity.
     *
     * @param capacity the initial capacity of the object.
     * @return         a newly generated structure.
     */
    @NotNull
    public T create(int capacity) {
        return supplier.apply(capacity);
    }
}
