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

package me.raatiniemi.jcmdr.argument;

import me.raatiniemi.jcmdr.argument.exception.UnevenQuotesException;

class ArgumentValueSanitizer {
    private static final String QUOTE_SINGLE = "'";
    private static final String QUOTE_DOUBLE = "\"";

    private final String argumentValue;

    private ArgumentValueSanitizer(String argumentValue) {
        this.argumentValue = argumentValue;
    }

    static String sanitize(String argumentValue) {
        ArgumentValueSanitizer sanitizer = new ArgumentValueSanitizer(argumentValue);

        return sanitizer.sanitize();
    }

    private static String trimBalancedQuotes(String argumentValue) {
        if (isEnclosedIn(argumentValue, QUOTE_DOUBLE)) {
            return trimBalancedQuotes(argumentValue, QUOTE_DOUBLE);
        }

        if (argumentValue.startsWith(QUOTE_DOUBLE) || argumentValue.endsWith(QUOTE_DOUBLE)) {
            throw new UnevenQuotesException("Double quotes used for argument value is uneven");
        }

        if (isEnclosedIn(argumentValue, QUOTE_SINGLE)) {
            return trimBalancedQuotes(argumentValue, QUOTE_SINGLE);
        }

        if (argumentValue.startsWith(QUOTE_SINGLE) || argumentValue.endsWith(QUOTE_SINGLE)) {
            throw new UnevenQuotesException("Double quotes used for argument value is uneven");
        }

        return argumentValue;
    }

    private static boolean isEnclosedIn(String argumentValue, String s) {
        return argumentValue.matches("^(" + s + ")(.*)(" + s + ")$");
    }

    private static String trimBalancedQuotes(String argumentValue, String s) {
        return argumentValue.replaceAll("^(" + s + ")(.*)(" + s + ")$", "$2");
    }

    private String sanitize() {
        return trimBalancedQuotes(argumentValue);
    }
}
