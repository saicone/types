package com.saicone.types;

import com.saicone.types.parser.EnumParser;
import com.saicone.types.parser.MapParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Represents an automatic detection type parser that retrieves the provided type parameter at runtime.<br>
 * This object also has methods to interact along with annotated type.
 *
 * @author Rubenicos
 *
 * @param <T> the defined annotated type in this object.
 */
public abstract class TypeOf<T> implements TypeParser<T> {

    private static final Map<Class<?>, Class<?>> NEAREST_TYPES = new HashMap<>();

    static {
        NEAREST_TYPES.put(Map.class, HashMap.class);
        NEAREST_TYPES.put(SortedMap.class, TreeMap.class);
        NEAREST_TYPES.put(NavigableMap.class, TreeMap.class);
        NEAREST_TYPES.put(Dictionary.class, Hashtable.class);
        NEAREST_TYPES.put(Collection.class, ArrayList.class);
        NEAREST_TYPES.put(List.class, ArrayList.class);
        NEAREST_TYPES.put(Set.class, HashSet.class);
        NEAREST_TYPES.put(SortedSet.class, TreeSet.class);
        NEAREST_TYPES.put(NavigableSet.class, TreeSet.class);
        NEAREST_TYPES.put(Queue.class, PriorityQueue.class);
        NEAREST_TYPES.put(Deque.class, LinkedList.class);
    }

    private final AnnotatedType annotated;

    private transient Boolean notNull;
    private transient Boolean nullable;
    private transient Class<?> rawType;
    private transient List<TypeOf<?>> parameters;
    private transient TypeParser<?> delegate;

    /**
     * Constructs a TypeOf object that extracts annotated type at runtime.
     */
    public TypeOf() {
        if (!TypeOf.class.equals(getClass().getSuperclass())) {
            throw new RuntimeException("The class " + getClass().getName() + " is not directly extended by " + TypeOf.class.getName());
        }
        final AnnotatedType annotatedType = getClass().getAnnotatedSuperclass();
        if (annotatedType instanceof AnnotatedParameterizedType) {
            this.annotated = ((AnnotatedParameterizedType) annotatedType).getAnnotatedActualTypeArguments()[0];
            return;
        }
        throw new IllegalStateException("The class " + getClass().getName() + " doesn't contains specified type parameters");
    }

    /**
     * Constructs a TypeOf object with provided annotated type.
     *
     * @param annotated the annotated type to use in this object.
     */
    public TypeOf(@NotNull AnnotatedType annotated) {
        this.annotated = annotated;
    }

    /**
     * Check if current type is a primitive class type.
     *
     * @return true if current type is primitive.
     */
    public boolean isPrimitive() {
       final Type type = getType();
       return type instanceof Class && ((Class<?>) type).isPrimitive();
    }

    /**
     * Check if current type is a class type.
     *
     * @return true if current type is class.
     */
    public boolean isRaw() {
        return getType() instanceof Class;
    }

    /**
     * Check if current type is an array type.
     *
     * @return true if current type is array.
     */
    public boolean isArray() {
        return getAnnotated() instanceof AnnotatedArrayType;
    }

    /**
     * Check if current type is a parameterized type.
     *
     * @return true if current type has parameters.
     */
    public boolean isParameterized() {
        return getAnnotated() instanceof AnnotatedParameterizedType;
    }

    /**
     * Check if current type is annotated as not-null object.
     *
     * @return true if current type is not null.
     */
    public boolean isNotNull() {
        if (notNull == null) {
            for (Annotation annotation : getAnnotated().getAnnotations()) {
                switch (annotation.annotationType().getSimpleName().toLowerCase()) {
                    case "notnull":
                    case "nonnull":
                    case "notnls":
                    case "nonnls":
                    case "required":
                    case "notempty":
                    case "notblank":
                    case "present":
                        notNull = true;
                        return true;
                    default:
                        break;
                }
            }
            notNull = false;
        }
        return notNull;
    }

    /**
     * Check if current type is annotated as nullable object.
     *
     * @return true if current type can be null.
     */
    public boolean isNullable() {
        if (nullable == null) {
            for (Annotation annotation : getAnnotated().getAnnotations()) {
                switch (annotation.annotationType().getSimpleName().toLowerCase()) {
                    case "nullable":
                    case "checkfornull":
                    case "optional":
                    case "nullallowed":
                    case "maybenull":
                        nullable = true;
                        return true;
                    default:
                        break;
                }
            }
            nullable = false;
        }
        return nullable;
    }

    @Override
    public boolean isInstance(@Nullable Object object) {
        return getRawType().isInstance(object);
    }

    /**
     * Get current annotated type object.
     *
     * @return the current annotated type.
     */
    @NotNull
    public AnnotatedType getAnnotated() {
        return annotated;
    }

    @Override
    public @NotNull Type getType() {
        return annotated.getType();
    }

    /**
     * Get the raw class value from current type.
     *
     * @return a raw class value.
     */
    @NotNull
    public Class<?> getRawType() {
        if (rawType == null) {
            Type type = getType();
            if (type instanceof GenericArrayType) {
                type = ((GenericArrayType) type).getGenericComponentType();
            }
            if (type instanceof ParameterizedType) {
                type = ((ParameterizedType) type).getRawType();
            }
            rawType = type instanceof Class ? (Class<?>) type : type.getClass();
        }
        return rawType;
    }

    /**
     * Get a not-null array of argument types.
     *
     * @return an array of type arguments.
     */
    @NotNull
    public Type[] getArguments() {
        final Type type = getType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return new Type[0];
    }

    /**
     * Get a not-null array of annotated argument types.
     *
     * @return an array of annotated arguments.
     */
    @NotNull
    public AnnotatedType[] getAnnotatedArguments() {
        final AnnotatedType type = getAnnotated();
        if (type instanceof AnnotatedParameterizedType) {
            return ((AnnotatedParameterizedType) type).getAnnotatedActualTypeArguments();
        }
        return new AnnotatedType[0];
    }

    /**
     * Get the list of type arguments wrapped as TypeOf object.
     *
     * @return a list of TypeOf objects.
     */
    @NotNull
    public List<TypeOf<?>> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<>();
            final AnnotatedType type = getAnnotated();
            if (type instanceof AnnotatedParameterizedType) {
                for (AnnotatedType annotated : ((AnnotatedParameterizedType) type).getAnnotatedActualTypeArguments()) {
                    parameters.add(new TypeOf<Object>(annotated){});
                }
            }
        }
        return parameters;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private TypeParser<?> getParser() {
        try {
            final Class<?> raw = getRawType();
            if (Map.class.isAssignableFrom(raw)) {
                return getMapParser((Class<? extends Map<?, ?>>) NEAREST_TYPES.getOrDefault(raw, raw));
            } else if (Collection.class.isAssignableFrom(raw)) {
                return getCollectionParser((Class<? extends Collection<?>>) NEAREST_TYPES.getOrDefault(raw, raw));
            } else if (raw.isArray()) {
                return getArrayParser(raw);
            } else if (Enum.class.isAssignableFrom(raw)) {
                return getEnumParser(raw);
            } else {
                return Types.of(raw);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @NotNull
    @SuppressWarnings("all")
    private TypeParser<?> getMapParser(@NotNull Class<? extends Map<?, ?>> raw) throws NoSuchMethodException {
        Constructor<? extends Map<?, ?>> c;
        try {
            c = raw.getDeclaredConstructor(int.class);
        } catch (NoSuchMethodException e) {
            c = raw.getDeclaredConstructor();
        }

        final Constructor<? extends Map<?, ?>> constructor = c;
        final TypeParser keyParser = getParameters().isEmpty() ? Types.OBJECT : getParameters().get(0).getParser();
        final TypeParser valueParser = getParameters().size() < 2 ? Types.OBJECT : getParameters().get(1).getParser();
        return new MapParser<>(capacity -> {
            try {
                if (constructor.getParameterCount() > 0) {
                    return (Map<Object, Object>) constructor.newInstance(capacity);
                } else {
                    return (Map<Object, Object>) constructor.newInstance();
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }, keyParser, valueParser);
    }

    @NotNull
    @SuppressWarnings("all")
    private TypeParser<?> getCollectionParser(@NotNull Class<? extends Collection<?>> raw) throws NoSuchMethodException {
        Constructor<? extends Collection<?>> c;
        try {
            c = raw.getDeclaredConstructor(int.class);
        } catch (NoSuchMethodException e) {
            c = raw.getDeclaredConstructor();
        }

        final TypeParser elementParser = getParameters().isEmpty() ? Types.OBJECT : getParameters().get(0).getParser();
        final Constructor<? extends Collection<?>> constructor = c;
        return elementParser.collection(raw, capacity -> {
            try {
                if (constructor.getParameterCount() > 0) {
                    return (Collection<Object>) constructor.newInstance(capacity);
                } else {
                    return (Collection<Object>) constructor.newInstance();
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    @NotNull
    @SuppressWarnings("all")
    private TypeParser<?> getArrayParser(@NotNull Class<?> raw) {
        final Class<?> component = raw.getComponentType();
        return Types.of(component).array();
    }

    @NotNull
    @SuppressWarnings("all")
    private TypeParser<?> getEnumParser(@NotNull Class<?> raw) {
        return EnumParser.of((Class) raw);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable T parse(@NotNull Object object) {
        if (delegate == null) {
            delegate = getParser();
        }
        return (T) delegate.parse(object);
    }

    @Override
    public String toString() {
        return annotated.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        TypeOf<?> typeOf = (TypeOf<?>) object;

        return annotated.equals(typeOf.annotated);
    }

    @Override
    public int hashCode() {
        return annotated.hashCode();
    }
}
