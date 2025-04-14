package com.saicone.types.annotation;

import org.intellij.lang.annotations.MagicConstant;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 * An annotated type with PatternFlags claims that any parsed value must have the declared flags.
 *
 * @author Rubenicos
 */
@Documented
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatternFlags {

    /**
     * Array of match flags, every value must be a bit mask that that may include
     * {@link Pattern#CASE_INSENSITIVE}, {@link Pattern#MULTILINE}, {@link Pattern#DOTALL},
     * {@link Pattern#UNICODE_CASE}, {@link Pattern#CANON_EQ}, {@link Pattern#UNIX_LINES},
     * {@link Pattern#LITERAL}, {@link Pattern#UNICODE_CHARACTER_CLASS}
     * and {@link Pattern#COMMENTS}
     *
     * @return the pattern flags.
     */
    int @MagicConstant(flagsFromClass = Pattern.class) [] value();

}
