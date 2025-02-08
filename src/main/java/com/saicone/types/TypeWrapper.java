package com.saicone.types;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Abstract class to apply lazy conversions between type of objects.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object, you can get it by {@link TypeWrapper#unwrap(Object)} the type B.
 * @param <B> the type of object that {@link TypeWrapper#wrap(Object)} type A, in other words, to show A as B.
 */
public abstract class TypeWrapper<A, B> {

    private static final TypeWrapper<?, ?> EMPTY = new TypeWrapper<Object, Object>() {
        @Override
        public Object wrap(Object t) {
            return t;
        }

        @Override
        public Object unwrap(Object t) {
            return t;
        }
    };
    private static final Map<Integer, TypeWrapper<?, ?>> WRAPPERS = new WeakHashMap<>();

    /**
     * Get a wrapper that don't do any type of conversion,
     * any object passed on this wrapped will be cast as required type.
     *
     * @return an empty {@link TypeWrapper}.
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <A, B> TypeWrapper<A, B> empty() {
        return (TypeWrapper<A, B>) EMPTY;
    }

    /**
     * Create a wrapper that parse type B using {@link TypeWrapper#wrap(Object)}
     * and cast any type to type A using {@link TypeWrapper#unwrap(Object)}.
     *
     * @param parser the type parser that convert objects into type B.
     * @return       a newly generated {@link TypeWrapper} or a cached one if it was created using the same {@link TypeParser}.
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <A, B> TypeWrapper<A, B> wrap(@NotNull TypeParser<B> parser) {
        // Cache wrapper if parser is applicable
        if (parser.getType() != null) {
            return (TypeWrapper<A, B>) WRAPPERS.computeIfAbsent(Objects.hash(0, parser.getType().hashCode()), k ->
                    new TypeWrapper<A, B>() {
                        @Override
                        public B wrap(Object object) {
                            return parser.parse(object);
                        }

                        @Override
                        public A unwrap(Object object) {
                            return (A) object;
                        }
                    }
            );
        }
        return new TypeWrapper<A, B>() {
            @Override
            public B wrap(Object object) {
                return parser.parse(object);
            }

            @Override
            public A unwrap(Object object) {
                return (A) object;
            }
        };
    }

    /**
     * Create a wrapper that parse type A using {@link TypeWrapper#unwrap(Object)}
     * and cast any type to type B using {@link TypeWrapper#wrap(Object)}.
     *
     * @param parser the type parser that convert objects into type A.
     * @return       a newly generated {@link TypeWrapper} or a cached one if it was created using the same {@link TypeParser}.
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <A, B> TypeWrapper<A, B> unwrap(@NotNull TypeParser<A> parser) {
        // Cache wrapper if parser is applicable
        if (parser.getType() != null) {
            return (TypeWrapper<A, B>) WRAPPERS.computeIfAbsent(Objects.hash(1, parser.getType().hashCode()), k ->
                    new TypeWrapper<A, B>() {
                        @Override
                        public B wrap(Object object) {
                            return (B) object;
                        }

                        @Override
                        public A unwrap(Object object) {
                            return parser.parse(object);
                        }
                    }
            );
        }
        return new TypeWrapper<A, B>() {
            @Override
            public B wrap(Object object) {
                return (B) object;
            }

            @Override
            public A unwrap(Object object) {
                return parser.parse(object);
            }
        };
    }

    /**
     * Create a wrapper that parse type A using {@link TypeWrapper#unwrap(Object)} and parse type B using {@link TypeWrapper#wrap(Object)}.
     *
     * @param parserA the type parser that convert objects into type A.
     * @param parserB the type parser that convert objects into type B.
     * @return        a newly generated {@link TypeWrapper} or a cached one if it was created using both of same {@link TypeParser}.
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <A, B> TypeWrapper<A, B> of(@NotNull TypeParser<A> parserA, @NotNull TypeParser<B> parserB) {
        // Cache wrapper if both type parsers are applicable
        if (parserA.getType() != null && parserB.getType() != null) {
            return (TypeWrapper<A, B>) WRAPPERS.computeIfAbsent(Objects.hash(parserA.getType(), parserB.getType()), k ->
                    new TypeWrapper<A, B>() {
                        @Override
                        public B wrap(Object object) {
                            return parserB.parse(object);
                        }

                        @Override
                        public A unwrap(Object object) {
                            return parserA.parse(object);
                        }
                    }
            );
        }
        return new TypeWrapper<A, B>() {
            @Override
            public B wrap(Object object) {
                return parserB.parse(object);
            }

            @Override
            public A unwrap(Object object) {
                return parserA.parse(object);
            }
        };
    }

    /**
     * Convert provided object into type B, this method is mostly known to represent type A as B.
     *
     * @param object the object to convert.
     * @return       an object type B.
     */
    public B wrap(Object object) {
        throw new IllegalStateException("The current type wrapper doesn't support wrapping");
    }

    /**
     * Convert provided object into type A, this method is mostly known to bring back type A from B representation.
     *
     * @param object the object to convert.
     * @return       an object type A.
     */
    public A unwrap(Object object) {
        throw new IllegalStateException("The current type wrapper doesn't support unwrapping");
    }
}
