package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AnyMap<K, V> implements AnyStructure<Map<K, V>>, Iterable<Map.Entry<K, V>> {

    private static final TypeParser<AnyMap<Object, Object>> PARSER = new TypeParser<AnyMap<Object, Object>>() {
        @Override
        public @NotNull Type getType() {
            return AnyMap.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public @Nullable AnyMap<Object, Object> parse(@NotNull Object object) {
            if (object instanceof Map) {
                return of((Map<Object, Object>) object);
            }
            return null;
        }
    };

    @NotNull
    public static TypeParser<AnyMap<Object, Object>> parser() {
        return PARSER;
    }

    @NotNull
    public static AnyMap<Object, Object> of(@NotNull Map<Object, Object> map) {
        return new AnyMap<>(map, Types.OBJECT, Types.OBJECT);
    }

    private final Map<K, V> value;
    private final TypeParser<K> keyParser;
    private final TypeParser<V> valueParser;

    public AnyMap(@NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser) {
        this(new HashMap<>(), keyParser, valueParser);
    }

    public AnyMap(@NotNull Map<K, V> value, @NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser) {
        this.value = value;
        this.keyParser = keyParser;
        this.valueParser = valueParser;
    }

    // Iterable

    @Override
    public @NotNull Iterator<Map.Entry<K, V>> iterator() {
        return value.entrySet().iterator();
    }

    // Any Object

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public Map<K, V> value() {
        return value;
    }

    // Any Structure

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public void clear() {
        value.clear();
    }

    // Any Map

    @NotNull
    public TypeParser<K> keyParser() {
        return keyParser;
    }

    @NotNull
    public TypeParser<V> valueParser() {
        return valueParser;
    }

    @NotNull
    public Set<K> keys() {
        return value.keySet();
    }

    @NotNull
    public Collection<V> values() {
        return value.values();
    }

    @NotNull
    public Set<Map.Entry<K, V>> entries() {
        return value.entrySet();
    }

    public boolean contains(@NotNull K key, @Nullable V value) {
        return Objects.equals(get(key), value);
    }

    public boolean containsKey(@NotNull K key) {
        return value.containsKey(key);
    }

    public boolean containsValue(@NotNull V value) {
        return this.value.containsValue(value);
    }

    public boolean containsAny(@NotNull AnyObject<?> key, @Nullable V value) {
        return contains(key.as(keyParser), value);
    }

    public boolean containsAny(@NotNull K key, @NotNull AnyObject<?> value) {
        return contains(key, value.isEmpty() ? null : value.as(valueParser));
    }

    public boolean containsAny(@NotNull AnyObject<?> key, @NotNull AnyObject<?> value) {
        return containsAny(key.as(keyParser), value);
    }

    public boolean containsAnyKey(@NotNull AnyObject<?> key) {
        return containsKey(key.as(keyParser));
    }

    public boolean containsAnyValue(@NotNull AnyObject<?> value) {
        return containsValue(value.as(valueParser));
    }

    @NotNull
    public AnyObject<V> get(@NotNull K key) {
        return AnyObject.of(value.get(key));
    }

    @NotNull
    public AnyObject<V> getAny(@NotNull AnyObject<?> key) {
        return get(key.as(keyParser));
    }

    @NotNull
    public AnyObject<V> set(@NotNull K key, @Nullable V value) {
        if (value == null) {
            return removeKey(key);
        }
        return AnyObject.of(this.value.put(key, value));
    }

    @NotNull
    public AnyObject<V> setAny(@NotNull AnyObject<?> key, @Nullable V value) {
        return set(key.as(keyParser), value);
    }

    @NotNull
    public AnyObject<V> setAny(@NotNull K key, @NotNull AnyObject<?> value) {
        return set(key, value.as(valueParser));
    }

    @NotNull
    public AnyObject<V> setAny(@NotNull AnyObject<?> key, @NotNull AnyObject<?> value) {
        return set(key.as(keyParser), value.as(valueParser));
    }

    public boolean remove(@NotNull K key, @NotNull V value) {
        final V result = this.value.get(key);
        if (Objects.equals(result, value)) {
            this.value.remove(key);
            return true;
        }
        return false;
    }

    @NotNull
    public AnyObject<V> removeKey(@NotNull K key) {
        return AnyObject.of(value.remove(key));
    }

    @NotNull
    public Set<K> removeValue(@Nullable V value) {
        final Set<K> keys = new HashSet<>();
        this.value.entrySet().removeIf(entry -> {
            if (Objects.equals(entry.getValue(), value)) {
                keys.add(entry.getKey());
                return true;
            }
            return false;
        });
        return keys;
    }

    public boolean removeAny(@NotNull AnyObject<?> key, @NotNull V value) {
        return remove(key.as(keyParser), value);
    }

    public boolean removeAny(@NotNull K key, @NotNull AnyObject<?> value) {
        return remove(key, value.as(valueParser));
    }

    public boolean removeAny(@NotNull AnyObject<?> key, @NotNull AnyObject<?> value) {
        return remove(key.as(keyParser), value.as(valueParser));
    }

    @NotNull
    public AnyObject<V> removeAnyKey(@NotNull AnyObject<?> key) {
        return removeKey(key.as(keyParser));
    }

    @NotNull
    public Set<K> removeAnyValue(@NotNull AnyObject<?> value) {
        return removeValue(value.as(valueParser));
    }

    public boolean removeIf(@NotNull BiPredicate<K, V> predicate) {
        return value.entrySet().removeIf(entry -> predicate.test(entry.getKey(), entry.getValue()));
    }

    public boolean retainIf(@NotNull BiPredicate<K, V> predicate) {
        return value.entrySet().removeIf(entry -> !predicate.test(entry.getKey(), entry.getValue()));
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyMap<K, V> move(@NotNull K fromKey, @NotNull K toKey) {
        final V value = this.value.remove(fromKey);
        if (value != null) {
            this.value.put(toKey, value);
        }
        return this;
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public AnyMap<K, V> move(@NotNull K fromKey, @NotNull K toKey, @NotNull UnaryOperator<V> mapper) {
        V value = this.value.remove(fromKey);
        if (value != null) {
            value = mapper.apply(value);
        }
        if (value != null) {
            this.value.put(toKey, value);
        }
        return this;
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public AnyMap<K, V> moveAny(@NotNull K fromKey, @NotNull K toKey, @NotNull Function<AnyObject<V>, V> mapper) {
        return move(fromKey, toKey, value -> mapper.apply(AnyObject.of(value)));
    }

    @NotNull
    @Contract("_, _, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> moveAnyList(@NotNull K fromKey, @NotNull K toKey, @NotNull TypeParser<E> elementParser, @NotNull Function<AnyList<E>, V> mapper) {
        return move(fromKey, toKey, value -> {
            if (value instanceof List) {
                return mapper.apply(new AnyList<>((List<E>) value, elementParser));
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> moveAnySet(@NotNull K fromKey, @NotNull K toKey, @NotNull TypeParser<E> elementParser, @NotNull Function<AnySet<E>, V> mapper) {
        return move(fromKey, toKey, value -> {
            if (value instanceof Set) {
                return mapper.apply(new AnySet<>((Set<E>) value, elementParser));
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public AnyMap<K, V> moveAnyMap(@NotNull K fromKey, @NotNull K toKey, @NotNull Function<AnyMap<K, V>, V> mapper) {
        return moveAnyMap(fromKey, toKey, keyParser, valueParser, mapper);
    }

    @NotNull
    @Contract("_, _, _, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <A, B> AnyMap<K, V> moveAnyMap(@NotNull K fromKey, @NotNull K toKey, @NotNull TypeParser<A> keyParser, @NotNull TypeParser<B> valueParser, @NotNull Function<AnyMap<A, B>, V> mapper) {
        return move(fromKey, toKey, value -> {
            if (value instanceof Map) {
                return mapper.apply(new AnyMap<>((Map<A, B>) value, keyParser, valueParser));
            }
            return value;
        });
    }

    @NotNull
    @Contract("_ -> this")
    public AnyMap<K, V> edit(@NotNull Consumer<AnyMap<K, V>> consumer) {
        consumer.accept(this);
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyMap<K, V> edit(@NotNull K key, @NotNull UnaryOperator<V> mapper) {
        V value = this.value.get(key);
        if (value != null) {
            value = mapper.apply(value);
        }
        if (value != null) {
            this.value.put(key, value);
        }
        return this;
    }

    @NotNull
    public AnyMap<K, V> editKeys(@NotNull UnaryOperator<K> mapper) {
        final boolean linked = this.value instanceof LinkedHashMap;
        final Map<K, V> modified = new HashMap<>();
        final Iterator<Map.Entry<K, V>> iterator = this.value.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<K, V> entry = iterator.next();
            final K key = entry.getKey();
            final K result = mapper.apply(key);
            if (key != result) {
                iterator.remove();
                if (result != null) {
                    modified.put(result, entry.getValue());
                }
            } else if (linked && !modified.isEmpty()) {
                iterator.remove();
                modified.put(entry.getKey(), entry.getValue());
            }
        }
        if (!modified.isEmpty()) {
            this.value.putAll(modified);
        }
        return this;
    }

    @NotNull
    public AnyMap<K, V> editValues(@NotNull UnaryOperator<V> mapper) {
        for (Map.Entry<K, V> entry : this.value.entrySet()) {
            final V value = entry.getValue();
            final V result = mapper.apply(value);
            if (value != result) {
                entry.setValue(result);
            }
        }
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> editElements(@NotNull K key, @NotNull UnaryOperator<E> mapper) {
        return edit(key, value -> {
            if (value instanceof List) {
                final List<E> list = (List<E>) value;
                list.replaceAll(mapper);
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> editElements(@NotNull K key, @NotNull TypeParser<E> elementParser, @NotNull Consumer<E> mapper) {
        return edit(key, value -> {
            if (value instanceof Iterable) {
                for (E e : (Iterable<E>) value) {
                    if (e != null) {
                        mapper.accept(elementParser.apply(e));
                    }
                }
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyMap<K, V> editAny(@NotNull K key, @NotNull Function<AnyObject<V>, V> mapper) {
        return edit(key, value -> mapper.apply(AnyObject.of(value)));
    }

    @NotNull
    @Contract("_, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> editAnyElements(@NotNull K key, @NotNull TypeParser<E> elementParser, @NotNull Function<AnyObject<E>, E> mapper) {
        return edit(key, value -> {
            if (value instanceof List) {
                final AnyList<E> list = new AnyList<>((List<E>) value, elementParser);
                for (int i = 0; i < list.size(); i++) {
                    list.editAny(i, mapper);
                }
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> editAnyList(@NotNull K key, @NotNull TypeParser<E> elementParser, @NotNull Function<AnyList<E>, V> mapper) {
        return edit(key, value -> {
            if (value instanceof List) {
                return mapper.apply(new AnyList<>((List<E>) value, elementParser));
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <E> AnyMap<K, V> editAnySet(@NotNull K key, @NotNull TypeParser<E> elementParser, @NotNull Function<AnySet<E>, V> mapper) {
        return edit(key, value -> {
            if (value instanceof Set) {
                return mapper.apply(new AnySet<>((Set<E>) value, elementParser));
            }
            return value;
        });
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyMap<K, V> editAnyMap(@NotNull K key, @NotNull Function<AnyMap<K, V>, V> mapper) {
        return editAnyMap(key, keyParser, valueParser, mapper);
    }

    @NotNull
    @Contract("_, _, _, _ -> this")
    @SuppressWarnings("unchecked")
    public <A, B> AnyMap<K, V> editAnyMap(@NotNull K key, @NotNull TypeParser<A> keyParser, @NotNull TypeParser<B> valueParser, @NotNull Function<AnyMap<A, B>, V> mapper) {
        return edit(key, value -> {
            if (value instanceof Map) {
                return mapper.apply(new AnyMap<>((Map<A, B>) value, keyParser, valueParser));
            }
            return value;
        });
    }

    @NotNull
    @Contract("_ -> this")
    public AnyMap<K, V> replace(@NotNull Map<? extends K, ? extends V> map) {
        value.putAll(map);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    public AnyMap<K, V> replaceDeep(@NotNull Map<? extends K, ? extends V> map) {
        replaceDeep((Map<Object, Object>) map, (Map<Object, Object>) value);
        return this;
    }

    @SuppressWarnings("unchecked")
    private static void replaceDeep(@NotNull Map<Object, Object> from, @NotNull Map<Object, Object> to) {
        Object tempValue;
        for (Map.Entry<Object, Object> entry : from.entrySet()) {
            try {
                if (entry.getValue() instanceof Map && (tempValue = to.get(entry.getKey())) instanceof Map) {
                    replaceDeep((Map<Object, Object>) entry.getValue(), (Map<Object, Object>) tempValue);
                } else {
                    to.put(entry.getKey(), entry.getValue());
                }
            } catch (Throwable ignored) { }
        }
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyMap<K, V> merge(@NotNull K key, @NotNull V value) {
        if (!this.value.containsKey(key)) {
            this.value.put(key, value);
        }
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    public AnyMap<K, V> merge(@NotNull Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (!this.value.containsKey(entry.getKey())) {
                this.value.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @SuppressWarnings("unchecked")
    public AnyMap<K, V> mergeDeep(@NotNull Map<? extends K, ? extends V> map) {
        mergeDeep((Map<Object, Object>) map, (Map<Object, Object>) value);
        return this;
    }

    @SuppressWarnings("unchecked")
    private static void mergeDeep(@NotNull Map<Object, Object> from, @NotNull Map<Object, Object> to) {
        Object tempValue;
        for (Map.Entry<Object, Object> entry : from.entrySet()) {
            try {
                if (!to.containsKey(entry.getKey())) {
                    to.put(entry.getKey(), entry.getValue());
                } else if (entry.getValue() instanceof Map && (tempValue = to.get(entry.getKey())) instanceof Map) {
                    mergeDeep((Map<Object, Object>) entry.getValue(), (Map<Object, Object>) tempValue);
                }
            } catch (Throwable ignored) { }
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnyMap)) return false;

        AnyMap<?, ?> anyMap = (AnyMap<?, ?>) o;
        return value.equals(anyMap.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
