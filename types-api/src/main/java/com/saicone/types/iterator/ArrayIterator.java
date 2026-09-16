package com.saicone.types.iterator;

import com.saicone.types.AnyIterator;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Abstract class to iterate into any array.<br>
 * Accepts any type of primitive and object array.
 *
 * @author Rubenicos
 *
 * @param <T> the array type.
 */
public abstract class ArrayIterator<T> extends AnyIterator<T> implements ListIterator<T> {

    private final boolean objectArray;

    private int currentIndex;
    private int lastIndex = -1;

    /**
     * Create a read-only array iterator.
     *
     * @param value the array to iterate.
     * @return      an array iterator.
     * @param <T>   the array type.
     */
    @NotNull
    public static <T> ArrayIterator<T> of(@NotNull Object value) {
        return new ArrayIterator<T>(value) {
            @Override
            public void setValue(Object value) {
                throw new UnsupportedOperationException("Cannot perform an array value replace with anonymous array iterator");
            }
        };
    }

    /**
     * Create a read-only array iterator with associated type parser.
     *
     * @param value  the array to iterate.
     * @param parser the parser to use while iteration.
     * @return       a parsed type array iterator.
     * @param <T>    the type of type parser.
     */
    @NotNull
    public static <T> ArrayIterator<T> of(@NotNull Object value, @NotNull TypeParser<T> parser) {
        return new ArrayIterator<T>(value) {
            @Override
            public void setValue(Object value) {
                throw new UnsupportedOperationException("Cannot perform an array value replace with anonymous array iterator");
            }

            @Override
            public T get(int index) {
                if (isObjectArray()) {
                    return parser.parse(((Object[]) getValue())[index]);
                } else {
                    return parser.parse(Array.get(getValue(), index));
                }
            }
        };
    }

    /**
     * Constructs an array iterator with provided array object.
     *
     * @param value the array to iterate.
     */
    public ArrayIterator(@NotNull Object value) {
        this(value, 0);
    }

    /**
     * Constructs an array iterator with provided array object and starting index.
     *
     * @param value        the array to iterate.
     * @param currentIndex the index to start the iteration.
     */
    public ArrayIterator(@NotNull Object value, int currentIndex) {
        super(value);
        this.objectArray = value instanceof Object[];
        this.currentIndex = currentIndex;
    }

    /**
     * Check if the current array iterator corresponds to an object array.
     *
     * @return true if the current array is an object array.
     */
    public boolean isObjectArray() {
        return objectArray;
    }

    /**
     * Returns the length of the specified array object, as an int.
     *
     * @return the length of the array.
     */
    public int size() {
        if (objectArray) {
            return ((Object[]) getValue()).length;
        } else {
            return Array.getLength(getValue());
        }
    }

    /**
     * Returns the value of the indexed component in the specified array object.
     * The value is automatically wrapped in an object if it has a primitive type.
     *
     * @param index the index.
     * @return      the (possibly wrapped) value of the indexed component in the specified array.
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (objectArray) {
            return (T) ((Object[]) getValue())[index];
        } else {
            return (T) Array.get(getValue(), index);
        }
    }

    @Override
    public boolean hasNext() {
        return currentIndex < size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        lastIndex = currentIndex;
        return get(currentIndex++);
    }

    @Override
    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    @Override
    public T previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        lastIndex = --currentIndex;
        return get(currentIndex);
    }

    @Override
    public int nextIndex() {
        return currentIndex;
    }

    @Override
    public int previousIndex() {
        return currentIndex - 1;
    }

    @Override
    public void remove() {
        if (lastIndex < 0) {
            throw new IllegalStateException();
        }

        remove(lastIndex);
        currentIndex = lastIndex;
        lastIndex = -1;
    }

    /**
     * Remove value form array at specified index.
     *
     * @param index the index to remove.
     */
    public void remove(int index) {
        final int size = size();
        if (size == 0 || index >= size) {
            throw new ConcurrentModificationException();
        }
        Object newArray = Array.newInstance(getValue().getClass().getComponentType(), size - 1);
        for (int i = 0; i < size; i++) {
            if (i < index) {
                Array.set(newArray, i, get(i));
            } else if (i > index) {
                Array.set(newArray, i - 1, get(i));
            }
        }
        this.value = newArray;
        setValue(newArray);
    }

    @Override
    public void set(T t) {
        if (lastIndex < 0) {
            throw new IllegalStateException();
        }
        set(lastIndex, t);
    }

    /**
     * Set value to array at specified index.
     *
     * @param index the index to set.
     * @param t     the value to set.
     */
    public void set(int index, T t) {
        if (objectArray) {
            ((Object[]) getValue())[index] = t;
        } else {
            Array.set(getValue(), index, t);
        }
    }

    @Override
    public void add(T t) {
        add(currentIndex, t);
        currentIndex++;
        lastIndex = -1;
    }

    /**
     * Add value to array at specified index.
     *
     * @param index the index to add.
     * @param t     the value to add.
     */
    public void add(int index, T t) {
        final int size = size();
        Object newArray = Array.newInstance(getValue().getClass().getComponentType(), size + 1);

        for (int i = 0; i < size; i++) {
            if (i < index) {
                Array.set(newArray, i, get(i));
            } else if (i == index) {
                Array.set(newArray, i, t);
            } else {
                Array.set(newArray, i + 1, get(i));
            }
        }

        setValue(newArray);
    }
}
