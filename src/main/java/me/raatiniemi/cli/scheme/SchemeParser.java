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
import java.util.ArrayList;
import java.util.List;

/**
 * Parse the argument scheme from a target class.
 */
class SchemeParser {
    private Class<?> target;

    SchemeParser(Class<?> target) {
        this.target = target;
    }

    List<SchemeArgument> parse() {
        List<SchemeArgument> arguments = new ArrayList<>();

        Method[] methods = this.target.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Argument.class)) {
                continue;
            }

            Argument argument = method.getAnnotation(Argument.class);

            SchemeArgument.Builder builder = new SchemeArgument.Builder();
            builder.shortName(argument.shortName());

            arguments.add(builder.build());
        }

        return arguments;
    }
}
