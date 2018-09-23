/*
 * Copyright (C) 2018 Tobias Raatiniemi
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

package me.raatiniemi.jcmdr.scheme.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate method to be included in scheme argument parsing.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    /**
     * Define the short name for the argument scheme.
     *
     * The short name must be either empty or a single character, if a
     * more descriptive name is needed, use {@link #longName}.
     *
     * @return Short name for the argument scheme.
     */
    String shortName() default "";

    /**
     * Define the long name for the argument scheme.
     *
     * The long name must be either empty or more than a one character,
     * if a single character is needed, use {@link #shortName}.
     *
     * @return Long name for the argument scheme.
     */
    String longName() default "";
}
