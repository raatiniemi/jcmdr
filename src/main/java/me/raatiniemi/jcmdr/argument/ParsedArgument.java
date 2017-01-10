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

package me.raatiniemi.jcmdr.argument;

/**
 * Represents an argument parsed from the arguments and argument scheme.
 */
public interface ParsedArgument {
    /**
     * Call the method associated with the parsed argument.
     *
     * @param target Target class on which to call the method.
     * @param <T>    Type reference of the target class.
     */
    <T> void call(T target);
}
