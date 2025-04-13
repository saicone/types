/**
 * This package provides the common type-related classes that are used across the project.
 *
 * <p>The primary class is {@link com.saicone.types.TypeParser} which is used to provide a conversion between objects,
 * next to {@link com.saicone.types.Types} being the library itself by providing a {@link com.saicone.types.TypeParser}
 * storage and registration, while {@link com.saicone.types.TypeOf} is a lazy conversion provider that uses registered
 * type parsers.
 *
 * <p>For iteration, the {@link com.saicone.types.IterableType} can be used by encapsulating values like iterables,
 * maps, arrays or single objects.
 *
 * <p>There are high-level interactions using {@link com.saicone.types.ValueType} that provides easy-to-use methods to
 * convert a delegated value, while TypeWrapper is a more complex conversion which is focused
 * on lazy values without convert a whole data structure.
 *
 * @author Rubenicos
 */
package com.saicone.types;