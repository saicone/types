package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import com.saicone.types.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a parser that convert objects into an array.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of array.
 * @param <C> the type of components in the array.
 */
public class ArrayParser<T, C> extends AllocParser<T> {

    private final Type type;
    private final TypeParser<C> componentParser;

    private static final Map<Integer, ArrayParser<?, ?>> PARSERS = new WeakHashMap<>();

    /**
     * Create an array parser using component type.
     *
     * @param componentType the component type of array.
     * @return              a newly generated array parser or a cached one.
     * @param <T> the type result of array.
     * @param <C> the type of components in the array.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T, C> ArrayParser<T, C> of(@NotNull Class<C> componentType) {
        final int key = Objects.hash(0, componentType);
        ArrayParser<?, ?> parser = PARSERS.get(key);
        if (parser == null) {
            parser = new ArrayParser<>(componentType, Types.of(componentType));
            PARSERS.put(key, parser);
        }
        return (ArrayParser<T, C>) parser;
    }

    /**
     * Create an array parser using element parser.
     *
     * @param componentParser the type parser to parse components.
     * @return                a newly generated array parser or a cached one.
     * @param <T> the type result of array.
     * @param <C> the type of components in the array.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T, C> ArrayParser<T, C> of(@NotNull TypeParser<C> componentParser) {
        Objects.requireNonNull(componentParser.getType(), "Cannot create array parser using a component parser that doesn't provide type");
        final int key = Objects.hash(1, componentParser);
        ArrayParser<?, ?> parser = PARSERS.get(key);
        if (parser == null) {
            parser = new ArrayParser<>((Class<C>) componentParser.getType(), componentParser);
            PARSERS.put(key, parser);
        }
        return (ArrayParser<T, C>) parser;
    }

    /**
     * Constructs an array parser with defined parameters.
     *
     * @param componentType   the component type of array.
     * @param componentParser the type parser to parse components.
     */
    @SuppressWarnings("unchecked")
    public ArrayParser(@NotNull Class<C> componentType, @NotNull TypeParser<C> componentParser) {
        this(capacity -> (T) Array.newInstance(componentType, capacity == null ? 0 : capacity), componentType, componentParser);
    }

    /**
     * Constructs an array parser with defined parameters.
     *
     * @param supplier        the function to create an array type with allocated size.
     * @param componentType   the component type of array.
     * @param componentParser the type parser to parse components.
     */
    public ArrayParser(@NotNull Function<Integer, T> supplier, @NotNull Class<C> componentType, @NotNull TypeParser<C> componentParser) {
        super(supplier);
        try {
            this.type = getArrayType(componentType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot create array type", e);
        }
        this.componentParser = componentParser;
    }

    private static Type getArrayType(@NotNull Class<?> componentType) throws ClassNotFoundException {
        // This can be simplified by using Class#descriptorString().replace('/', '.') from Java +12
        if (componentType.isArray()) {
            return Class.forName("[" + componentType.getName());
        } else if (componentType.isPrimitive()) {
            // This can be simplified by using Wrapper#forPrimitiveType() from sun API
            if (componentType == long.class) {
                return Class.forName("[J");
            } else if (componentType == boolean.class) {
                return Class.forName("[Z");
            } else {
                return Class.forName("[" + Character.toUpperCase(componentType.getSimpleName().charAt(0)));
            }
        } else {
            return Class.forName("[L" + componentType.getName() + ";");
        }
    }

    @Override
    @Nullable
    public Type getType() {
        return type;
    }

    /**
     * Get the type associated with the component type of this array parser.
     *
     * @return a type object if present, null otherwise.
     */
    @Nullable
    public Type getComponentType() {
        return componentParser.getType();
    }

    /**
     * Get the type parser to parse components.
     *
     * @return a type parser.
     */
    @NotNull
    public TypeParser<C> getComponentParser() {
        return componentParser;
    }

    /**
     * Check if the given object is instance of component parser type.
     *
     * @param object the object to check.
     * @return       true if the object is an instance of component parser type.
     */
    public boolean isComponentInstance(@Nullable Object object) {
        return componentParser.isInstance(object);
    }

    @Override
    public @NotNull T parse(@Nullable Object object) {
        return parseEach(object, (element, parser) -> parser.parse(element));
    }

    /**
     * Parse the provided object into required type by converting each applicable component using defined function.
     *
     * @param object   the object to parse.
     * @param function the function that convert a component and the parser for its object to required component type.
     * @return         a converted value type, empty by default.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public T parseEach(@Nullable Object object, @NotNull BiFunction<Object, TypeParser<C>, C> function) {
        if (object == null) {
            return create();
        } else if (isInstance(object)) {
            return (T) object;
        }
        final AnyIterable<Object> iterable = AnyIterable.of(object);
        final int size = iterable.size(); // Initial capacity
        T array = create(size);

        int i = 0;
        for (Object element : iterable) {
            if (i >= size) {
                T arrayCopy = create(i + 1);
                System.arraycopy(array, 0, arrayCopy, 0, i);
                array = arrayCopy;
            }
            Array.set(array, i, function.apply(element, componentParser));
            i++;
        }

        return array;
    }
}
