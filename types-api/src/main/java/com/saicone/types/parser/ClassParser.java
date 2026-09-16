package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a parser that try to convert any String representation of Class name.<br>
 * This parser is compatible with qualified name, file path and class descriptor.
 *
 * @author Rubenicos
 */
public class ClassParser implements TypeParser<Class<?>> {

    private static final Map<String, Class<?>> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put("C", char.class);
        PRIMITIVES.put("char", char.class);
        PRIMITIVES.put("char[]", char[].class);
        PRIMITIVES.put("Z", boolean.class);
        PRIMITIVES.put("boolean", boolean.class);
        PRIMITIVES.put("boolean[]", boolean[].class);
        PRIMITIVES.put("B", byte.class);
        PRIMITIVES.put("byte", byte.class);
        PRIMITIVES.put("byte[]", byte[].class);
        PRIMITIVES.put("S", short.class);
        PRIMITIVES.put("short", short.class);
        PRIMITIVES.put("short[]", short[].class);
        PRIMITIVES.put("I", int.class);
        PRIMITIVES.put("int", int.class);
        PRIMITIVES.put("int[]", int[].class);
        PRIMITIVES.put("J", long.class);
        PRIMITIVES.put("long", long.class);
        PRIMITIVES.put("long[]", long[].class);
        PRIMITIVES.put("F", float.class);
        PRIMITIVES.put("float", float.class);
        PRIMITIVES.put("float[]", float[].class);
        PRIMITIVES.put("D", double.class);
        PRIMITIVES.put("double", double.class);
        PRIMITIVES.put("double[]", double[].class);
        PRIMITIVES.put("void", void.class);
    }

    /**
     * {@link ClassParser} public instance that doesn't initialize classes.
     */
    public static final ClassParser INSTANCE = new ClassParser(false, ClassParser.class.getClassLoader());

    /**
     * Create a class parser with given class loader.<br>
     * This method creates a class parser that doesn't initialize classes.
     *
     * @param classLoader class loader from which the classes must be loaded.
     * @return            a newly created class parser.
     */
    @NotNull
    public static ClassParser of(@NotNull ClassLoader classLoader) {
        return new ClassParser(false, classLoader);
    }

    private final boolean initialize;
    private final ClassLoader classLoader;

    /**
     * Constructs a class parser with given parameters.
     *
     * @param initialize  if true any created class object will be initialized.
     * @param classLoader class loader from which the classes must be loaded.
     */
    public ClassParser(boolean initialize, @NotNull ClassLoader classLoader) {
        this.initialize = initialize;
        this.classLoader = classLoader;
    }

    /**
     * Get initialization state of this parser.
     *
     * @return true if any created class object will be initialized.
     */
    public boolean isInitialize() {
        return initialize;
    }

    @Override
    public @Nullable Type getType() {
        return Class.class;
    }

    /**
     * Get the class loaded used in this parser.
     *
     * @return a class loader from which the classes must be loaded.
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public @Nullable Class<?> parse(@NotNull Object object) {
        final Object first = AnyIterable.of(object).first();
        if (first == null) {
            return null;
        }

        if (first instanceof Class) {
            return (Class<?>) first;
        }

        try {
            return forName(getReadableName(String.valueOf(first)));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the {@code Class} object associated with the given string name,
     * meaning this method is compatible with primitive types and readable array
     * name declaration such as {@code java.lang.String[]} or {@code int[][]}.
     *
     * @param name the name of the desired class.
     * @return     class object representing the desired class.
     * @throws ClassNotFoundException if the class cannot be located.
     */
    @NotNull
    public Class<?> forName(@NotNull String name) throws ClassNotFoundException {
        final Class<?> primitive = PRIMITIVES.get(name);
        if (primitive != null) {
            return primitive;
        }
        if (name.endsWith("[]")) {
            return getArrayType(forName(name.substring(0, name.length() - 2)));
        }
        return Class.forName(name, isInitialize(), getClassLoader());
    }

    /**
     * Convert any class name declaration into its readable name.<br>
     * The result of this method may not be compatible with {@link Class#forName(String)},
     * but is always compatible with {@link ClassParser#forName(String)}.
     *
     * @param name the class name to convert.
     * @return     a readable class name.
     */
    @NotNull
    public static String getReadableName(@NotNull String name) {
        String className = name.replace('/', '.').replace('\\', '.');
        if (className.endsWith(";")) {
            className = className.substring(0, className.length() - 1);
            if (className.startsWith("L")) { // Internal class name
                className = className.substring(1);
            }
        } else if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - 6);
        } else if (className.endsWith(".java")) {
            className = className.substring(0, className.length() - 5);
        }
        if (className.startsWith(".")) {
            className = className.substring(1);
        } else if (className.length() > 3 && className.charAt(1) == ':' && className.charAt(2) == '.') {
            className = className.substring(3);
        }
        if (className.startsWith("[")) { // Array type declaration
            for (int i = 0; i < className.length(); i++) {
                final char c = className.charAt(i);
                if (c == '[') {
                    continue;
                } else if (c == 'L') { // Object array
                    className = className.substring(i + 1) + repeat("[]", i);
                } else { // Primitive array
                    final Class<?> primitive = PRIMITIVES.get(className.substring(i));
                    if (primitive != null) {
                        className = primitive.getSimpleName() + repeat("[]", i);
                    }
                }
                break;
            }
        }
        return className;
    }

    /**
     * Create an array class type for the given component type.<br>
     * This method is almost compatible with every component type, only restricted by Java language itself.
     *
     * @param componentType the component to create an array of.
     * @return              a newly generated array class type.
     * @throws ClassNotFoundException if the class cannot be located.
     */
    @NotNull
    public static Class<?> getArrayType(@NotNull Class<?> componentType) throws ClassNotFoundException {
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

    @NotNull
    private static String repeat(@NotNull String s, int count) {
        // This can be simplified by using String#repeat() from Java +11
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(s);
        }
        return builder.toString();
    }
}
