package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Represents a {@link Map} of key and value type A as a {@link Map} of key and value type B.
 *
 * @author Rubenicos
 *
 * @param <KeyA> the base type of key object.
 * @param <KeyB> the type of object to represent key A.
 * @param <ValueA> the base type of value object.
 * @param <ValueB> the type of object to represent value A.
 */
public class WrappedMap<KeyA, KeyB, ValueA, ValueB> extends WrappedPair<KeyA, KeyB, ValueA, ValueB> implements Map<KeyB, ValueB> {

    /**
     * Constructs a map of key and value A with its types represented as key and value B.
     *
     * @param delegated    the delegated map that will be wrapped in the instance.
     * @param keyWrapper   the type wrapper to represent delegated map key B type and convert it back to key A type.
     * @param valueWrapper the type wrapper to represent delegated map value B type and convert it back to value A type.
     */
    public WrappedMap(@NotNull Map<KeyA, ValueA> delegated, @NotNull TypeWrapper<KeyA, KeyB> keyWrapper, @NotNull TypeWrapper<ValueA, ValueB> valueWrapper) {
        super(delegated, keyWrapper, valueWrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Map<KeyA, ValueA> getDelegated() {
        return (Map<KeyA, ValueA>) super.getDelegated();
    }

    @Override
    public int size() {
        return getDelegated().size();
    }

    @Override
    public boolean isEmpty() {
        return getDelegated().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return getDelegated().containsKey(unwrapLeft(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return getDelegated().containsValue(unwrapRight(value));
    }

    @Override
    public ValueB get(Object key) {
        return wrapRight(getDelegated().get(key));
    }

    @Override
    public @Nullable ValueB put(KeyB key, ValueB value) {
        return wrapRight(getDelegated().put(unwrapLeft(key), unwrapRight(value)));
    }

    /**
     * Same as {@link Map#put(Object, Object)} with any Object compatibility.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return      the previous value associated with {@code key}, or {@code null} if there was no mapping for {@code key}.
     */
    @Nullable
    public ValueB putAny(Object key, Object value) {
        return wrapRight(getDelegated().put(unwrapLeft(key), unwrapRight(value)));
    }

    @Override
    public ValueB remove(Object key) {
        return wrapRight(getDelegated().remove(unwrapLeft(key)));
    }

    @Override
    public void putAll(@NotNull Map<? extends KeyB, ? extends ValueB> m) {
        putAnyAll(m);
    }

    /**
     * Same as {@link Map#putAll(Map)} with any Object compatibility.
     *
     * @param m mappings to be stored in this map
     */
    @SuppressWarnings("unchecked")
    public void putAnyAll(@NotNull Map<?, ?> m) {
        if (m instanceof WrappedMap && isSimilar((WrappedPair<?, ?, ?, ?>) m)) {
            getDelegated().putAll((Map<? extends KeyA, ? extends ValueA>) ((WrappedMap<?, ?, ?, ?>) m).getDelegated());
            return;
        }
        for (Map.Entry<?, ?> entry : m.entrySet()) {
            getDelegated().put(unwrapLeft(entry.getKey()), unwrapRight(entry.getValue()));
        }
    }

    @Override
    public void clear() {
        getDelegated().clear();
    }

    @Override
    public @NotNull Set<KeyB> keySet() {
        return new WrappedSet<>(getDelegated().keySet(), getLeftWrapper());
    }

    @Override
    public @NotNull Collection<ValueB> values() {
        return new WrappedCollection<>(getDelegated().values(), getRightWrapper());
    }

    @Override
    public @NotNull Set<Map.Entry<KeyB, ValueB>> entrySet() {
        return new WrappedSet<>(getDelegated().entrySet(), Entry.wrapper(getLeftWrapper(), getRightWrapper()));
    }

    /**
     * Represents a {@link Map.Entry} of key and value type A as a {@link Map.Entry} of key and value type B.
     *
     * @author Rubenicos
     *
     * @param <KeyA> the base type of key object.
     * @param <KeyB> the type of object to represent key A.
     * @param <ValueA> the base type of value object.
     * @param <ValueB> the type of object to represent value A.
     */
    public static class Entry<KeyA, KeyB, ValueA, ValueB> extends WrappedPair<KeyA, KeyB, ValueA, ValueB> implements Map.Entry<KeyB, ValueB> {

        /**
         * Create a type wrapper that parse entries with key and value A type using {@link TypeWrapper#unwrap(Object)} and parse key and value B type using {@link TypeWrapper#wrap(Object)}.
         *
         * @param keyWrapper   the type wrapper to represent entry key B type and convert it back to key A type.
         * @param valueWrapper the type wrapper to represent entry value B type and convert it back to value A type.
         * @return             a newly generated {@link TypeWrapper}.
         * @param <KeyA> the base type of key object.
         * @param <KeyB> the type of object to represent key A.
         * @param <ValueA> the base type of value object.
         * @param <ValueB> the type of object to represent value A.
         */
        @NotNull
        static <KeyA, KeyB, ValueA, ValueB> TypeWrapper<Map.Entry<KeyA, ValueA>, Map.Entry<KeyB, ValueB>> wrapper(@NotNull TypeWrapper<KeyA, KeyB> keyWrapper, @NotNull TypeWrapper<ValueA, ValueB> valueWrapper) {
            return new TypeWrapper<Map.Entry<KeyA, ValueA>, Map.Entry<KeyB, ValueB>>() {
                @Override
                @SuppressWarnings("unchecked")
                public Map.Entry<KeyB, ValueB> wrap(Object entryA) {
                    return new Entry<>((Map.Entry<KeyA, ValueA>) entryA, keyWrapper, valueWrapper);
                }

                @Override
                @SuppressWarnings("unchecked")
                public Map.Entry<KeyA, ValueA> unwrap(Object entryB) {
                    if (entryB instanceof Entry) {
                        if (((Entry<?, ?, ?, ?>) entryB).getLeftWrapper() == keyWrapper && ((Entry<?, ?, ?, ?>) entryB).getRightWrapper() == valueWrapper) {
                            return (Map.Entry<KeyA, ValueA>) ((Entry<?, ?, ?, ?>) entryB).getDelegated();
                        }
                    }
                    final Map.Entry<KeyB, ValueB> entry = (Map.Entry<KeyB, ValueB>) entryB;
                    return new AbstractMap.SimpleEntry<>(keyWrapper.unwrap(entry.getKey()), valueWrapper.unwrap(entry.getValue()));
                }
            };
        }

        /**
         * Constructs an entry of key and value A with its types represented as key and value B.
         *
         * @param delegated    the delegated entry that will be wrapped in the instance.
         * @param keyWrapper   the type wrapper to represent delegated entry key B type and convert it back to key A type.
         * @param valueWrapper the type wrapper to represent delegated entry value B type and convert it back to value A type.
         */
        public Entry(@NotNull Map.Entry<KeyA, ValueA> delegated, @NotNull TypeWrapper<KeyA, KeyB> keyWrapper, @NotNull TypeWrapper<ValueA, ValueB> valueWrapper) {
            super(delegated, keyWrapper, valueWrapper);
        }

        @Override
        @SuppressWarnings("unchecked")
        public @NotNull Map.Entry<KeyA, ValueA> getDelegated() {
            return (Map.Entry<KeyA, ValueA>) super.getDelegated();
        }

        @Override
        public KeyB getKey() {
            return wrapLeft(getDelegated().getKey());
        }

        @Override
        public ValueB getValue() {
            return wrapRight(getDelegated().getValue());
        }

        @Override
        public ValueB setValue(ValueB value) {
            return wrapRight(getDelegated().setValue(unwrapRight(value)));
        }

        /**
         * Same as {@link Map.Entry#setValue(Object)} with any Object compatibility.
         *
         * @param value new value to be stored in this entry
         * @return      old value corresponding to the entry
         */
        public ValueB setAnyValue(Object value) {
            return wrapRight(getDelegated().setValue(unwrapRight(value)));
        }
    }
}
