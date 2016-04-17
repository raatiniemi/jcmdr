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

package me.raatiniemi.cli.scheme;

import me.raatiniemi.cli.scheme.annotation.Argument;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parse the argument scheme from a target class.
 */
class SchemeParser {
    private Class<?> target;

    SchemeParser(Class<?> target) {
        this.target = target;
    }

    List<SchemeArgument> parse() {
        return getMethods().stream()
                .filter(method -> method.isAnnotationPresent(Argument.class))
                .map(method -> method.getAnnotation(Argument.class))
                .map(annotation ->
                        new SchemeArgument.Builder()
                                .shortName(annotation.shortName())
                                .longName(annotation.longName())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private List<Method> getMethods() {
        return sortMethodsByName(
                Arrays.asList(this.target.getMethods())
        );
    }

    private List<Method> sortMethodsByName(List<Method> methods) {
        Collections.sort(
                methods,
                (lhs, rhs) -> lhs.getName().compareTo(rhs.getName())
        );

        return methods;
    }
}
