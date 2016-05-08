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

import java.lang.reflect.Method;

/**
 * Builds SchemeArgument from outside of the package when testing.
 */
public class SchemeArgumentBuilder {
    public static SchemeArgument build(
            String shortName,
            String longName,
            Method methodReference
    ) {
        return new SchemeArgument.Builder()
                .shortName(shortName)
                .longName(longName)
                .methodReference(methodReference)
                .build();
    }
}