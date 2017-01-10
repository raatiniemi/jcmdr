/*
 * Copyright (C) 2017 Tobias Raatiniemi
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

/**
 * Represent argument scheme parsed from method with {@link Argument} annotation.
 */
public interface SchemeArgument {
    /**
     * Validate the argument, and value types, against the argument scheme.
     *
     * @param argument           Argument to validate.
     * @param argumentValueTypes Argument value types to validate.
     * @return True if argument, and value types, match the argument scheme, otherwise false.
     */
    boolean validate(String argument, Class<?>... argumentValueTypes);

    /**
     * Call the method associated with the argument scheme.
     *
     * @param target        Target class on which to call the method.
     * @param argumentValue Argument value with which to call the method.
     * @param <T>           Type reference of the the target class.
     */
    <T> void call(T target, String argumentValue);
}
