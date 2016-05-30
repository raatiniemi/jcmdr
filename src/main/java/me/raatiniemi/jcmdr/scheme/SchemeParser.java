/*
 * Copyright (C) 2016 Tobias Raatiniemi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.raatiniemi.jcmdr.scheme;

import me.raatiniemi.jcmdr.scheme.annotation.Argument;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Parse the argument scheme from a target class.
 */
public class SchemeParser {
    private Class<?> target;

    public SchemeParser(Class<?> target) {
        this.target = target;
    }

    private static Predicate<Method> includeMethodsWithAnnotation() {
        return method -> method.isAnnotationPresent(Argument.class);
    }

    private static Comparator<Method> sortMethodsByName() {
        return (lhs, rhs) -> lhs.getName().compareTo(rhs.getName());
    }

    private static Function<Method, SchemeArgument> buildSchemeArgumentFromMethod() {
        return method -> {
            Argument argument = method.getAnnotation(Argument.class);

            return new SchemeArgument.Builder()
                    .shortName(argument.shortName())
                    .longName(argument.longName())
                    .methodReference(method)
                    .build();
        };
    }

    public List<SchemeArgument> parse() {
        return getMethods().stream()
                .filter(includeMethodsWithAnnotation())
                .sorted(sortMethodsByName())
                .map(buildSchemeArgumentFromMethod())
                .collect(Collectors.toList());
    }

    private List<Method> getMethods() {
        return Arrays.asList(this.target.getMethods());
    }
}