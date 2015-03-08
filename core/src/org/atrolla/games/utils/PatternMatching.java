package org.atrolla.games.utils;

/**
 * Created by MicroOnde on 08/03/2015.
 */

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface PatternMatching<T, R> {

    static <T, R> PatternMatching<T, R> when(Predicate<T> predicate, Function<T, R> action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);

        return value -> {
            if (predicate.test(value)) {
                return Optional.of(action.apply(value));
            } else {
                return Optional.empty();
            }
        };
    }

    default PatternMatching<T, R> otherwise(Function<T, R> action) {
        Objects.requireNonNull(action);

        return value -> {
            final Optional<R> result = matches(value);
            if (result.isPresent()) {
                return result;
            } else {
                return Optional.of(action.apply(value));
            }
        };
    }

    default PatternMatching<T, R> orWhen(Predicate<T> predicate, Function<T, R> action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);

        return value -> {
            final Optional<R> result = matches(value);
            if (result.isPresent()) {
                return result;
            } else {
                if (predicate.test(value)) {
                    return Optional.of(action.apply(value));
                } else {
                    return Optional.empty();
                }
            }
        };

    }

    Optional<R> matches(T value);

}