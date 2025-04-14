package com.saicone.types;

import com.saicone.types.iterator.ArrayIterator;
import com.saicone.types.iterator.SingleIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

/**
 * Functional interface to allow any object to be targeted on the enhanced for statement.<br>
 * This interface can iterate hover {@link Iterable}, {@link Map}, {@code Object[]}, any primitive array and single objects.
 *
 * @author Rubenicos
 *
 * @param <T> the iterable object type.
 */
@FunctionalInterface
public interface IterableType<T> extends Iterable<T> {

    /**
     * Create a read-only iterable type object.
     *
     * @param value the value to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Object> of(@NotNull Object value) {
        if (value instanceof IterableType) {
            return of(((IterableType<?>) value).getValue());
        }
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive char array.
     *
     * @param value the char array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Character> of(char[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive boolean array.
     *
     * @param value the boolean array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Boolean> of(boolean[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive byte array.
     *
     * @param value the byte array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Byte> of(byte[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive short array.
     *
     * @param value the short array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Short> of(short[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive int array.
     *
     * @param value the int array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Integer> of(int[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive float array.
     *
     * @param value the float array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Float> of(float[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive long array.
     *
     * @param value the long array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Long> of(long[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with primitive double array.
     *
     * @param value the double array to iterate over.
     * @return      a new iterable type.
     */
    @NotNull
    static IterableType<Double> of(double[] value) {
        return ofAny(value);
    }

    /**
     * Create a read-only iterable type with object array.
     *
     * @param value the object array to iterate over.
     * @return      a new iterable type.
     * @param <T>   the iterable array type.
     */
    @NotNull
    static <T> IterableType<T> of(@NotNull T[] value) {
        return ofAny(value);
    }

    /**
     * Create an iterable type that can remove objects from provided iterable parameter.
     *
     * @param value the iterable to iterate over.
     * @return      a new iterable type.
     * @param <E>   the iterable object type.
     */
    @NotNull
    static <E> IterableType<E> of(@NotNull Iterable<E> value) {
        return ofAny(value);
    }

    /**
     * Create an iterable type that can remove objects from provided map.
     *
     * @param value the map to iterate over.
     * @return      a new iterable type.
     * @param <K>   the type of keys maintained by the map.
     * @param <V>   the type of mapped values.
     */
    @NotNull
    static <K, V> IterableType<Map.Entry<K, V>> of(@NotNull Map<K, V> value) {
        return ofAny(value);
    }

    /**
     * Create an unchecked iterable type from any object.
     *
     * @param value the object to iterate over.
     * @return      a new iterable type.
     * @param <T>   the iterable object type.
     */
    @NotNull
    static <T> IterableType<T> ofAny(@NotNull Object value) {
        return () -> value;
    }

    /**
     * Get the object that can be iterated.
     *
     * @return an iterable object.
     */
    Object getValue();

    /**
     * Set the object that can be iterated.
     *
     * @param value an iterable object.
     * @return      an object.
     */
    default Object setValue(Object value) {
        throw new IllegalStateException("The current iterable type doesn't allow value override");
    }

    /**
     * Check if the current object can be iterated using for statement.<br>
     * This condition can be applied to any {@link Iterable} type, map or array.
     *
     * @return true if the object can be iterated.
     */
    default boolean isIterable() {
        final Object value = getValue();
        return value != null && (value instanceof Iterable || value instanceof Map || value.getClass().isArray());
    }

    /**
     * Check if the current object can return a {@link ListIterator} on {@link #listIterator()}.<br>
     * This condition can be applied to any {@link List} type or array.
     *
     * @return true if the object can return a {@link ListIterator}.
     */
    default boolean isListIterable() {
        final Object value = getValue();
        return value != null && (value instanceof List || value.getClass().isArray());
    }

    /**
     * Same has {@link #isIterable()} but with inverted result.
     *
     * @return true if the object can't be iterated.
     */
    default boolean isNotIterable() {
        return !isIterable();
    }

    /**
     * Same has {@link #isListIterable()} but with inverted result.
     *
     * @return true if the object can't return a {@link ListIterator}.
     */
    default boolean isNotListIterable() {
        return !isListIterable();
    }

    @Override
    @SuppressWarnings("unchecked")
    default @NotNull Iterator<T> iterator() {
        Objects.requireNonNull(getValue(), "Cannot iterate over empty object");
        final Object value = getValue();
        if (value instanceof Iterable) {
            return ((Iterable<T>) value).iterator();
        } else if (value instanceof Map) {
            return (Iterator<T>) ((Map<?, ?>) value).entrySet().iterator();
        } else if (value instanceof Object[] || value.getClass().isArray()) {
            return new ArrayIterator<T>(value) {
                @Override
                public void setValue(Object value) {
                    IterableType.this.setValue(value);
                }
            };
        } else {
            return new SingleIterator<T>(value) {
                @Override
                public void setValue(Object value) {
                    IterableType.this.setValue(value);
                }
            };
        }
    }

    /**
     * Return a list iterator over the elements of the current value<br>
     * This method can return a properly made list iterator if the current value is a list or an array.
     *
     * @return      a newly generated list iterator.
     * @throws IllegalStateException if the current value is no compatible with list iterator.
     */
    @NotNull
    default ListIterator<T> listIterator() {
        return listIterator(0);
    }

    /**
     * Return a list iterator over the elements of the current value, starting at the specified position in the value.<br>
     * This method can return a properly made list iterator if the current value is a list or an array.
     *
     * @param index the index of the first element to be returned from the list iterator.
     * @return      a newly generated list iterator.
     * @throws IllegalStateException if the current value is no compatible with list iterator.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default ListIterator<T> listIterator(int index) throws IllegalStateException {
        Objects.requireNonNull(getValue(), "Cannot iterate over empty object");
        final Object value = getValue();
        if (value instanceof List) {
            return ((List<T>) value).listIterator(index);
        } else if (value instanceof Object[] || value.getClass().isArray()) {
            return new ArrayIterator<T>(value, index) {
                @Override
                public void setValue(Object value) {
                    IterableType.this.setValue(value);
                }
            };
        } else {
            throw new IllegalStateException("The object type " + value.getClass().getName() + " is not compatible with list iteration");
        }
    }

    /**
     * Get the size of the current object.
     *
     * @return a number that represent the current amount of values the object has.
     */
    default int size() {
        Objects.requireNonNull(getValue(), "Cannot get the size of a null object");
        final Object value = getValue();
        if (value instanceof Collection) {
            return ((Collection<?>) value).size();
        } else if (value instanceof Map) {
            return ((Map<?, ?>) value).size();
        } else if (value instanceof Object[] || value.getClass().isArray()) {
            return Array.getLength(value);
        } else {
            // Single value
            return 1;
        }
    }

    /**
     * Convert the current type into the first object representation,
     * this means that any object compatible with this instance will
     * be iterated to get the first object.
     *
     * @return the first object, null if current value is empty or null.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    default T first() {
        final Object value = getValue();
        if (value instanceof Iterable) {
            final Iterator<T> iterator = ((Iterable<T>) value).iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        } else if (value instanceof Object[]) {
            if (((Object[]) value).length > 0) {
                return (T) ((Object[]) value)[0];
            }
        } else if (value.getClass().isArray()) {
            if (Array.getLength(value) > 0) {
                return (T) Array.get(value, 0);
            }
        } else {
            return (T) value;
        }
        return null;
    }


    /**
     * Convert the current type into a single object representation,
     * this means that any iterable object will be converted into
     * single object by taking the first iterated value.
     *
     * @return a single object.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    default T single() {
        final Object value = getValue();
        if (value instanceof Iterable) {
            final Iterator<Object> iterator = ((Iterable<Object>) value).iterator();
            final Object obj;
            if (iterator.hasNext() && (obj = iterator.next()) != null) {
                return (T) obj;
            } else {
                return null;
            }
        } else {
            return (T) value;
        }
    }
}