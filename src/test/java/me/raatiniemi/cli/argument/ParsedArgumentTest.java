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

package me.raatiniemi.cli.argument;

import me.raatiniemi.cli.scheme.SchemeArgument;
import me.raatiniemi.cli.scheme.SchemeArgumentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ParsedArgumentTest {
    private String message;
    private Boolean expected;
    private ParsedArgument parsedArgument;
    private Object compareTo;

    public ParsedArgumentTest(
            String message,
            Boolean expected,
            ParsedArgument parsedArgument,
            Object compareTo
    ) {
        this.compareTo = compareTo;
        this.expected = expected;
        this.message = message;
        this.parsedArgument = parsedArgument;
    }

    private static SchemeArgument buildSchemeArgument(
            String shortName,
            String longName
    ) {
        return SchemeArgumentBuilder.build(shortName, longName, null);
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        ParsedArgument parsedArgument = new ParsedArgument(buildSchemeArgument("d", "debug"));

        return Arrays.asList(
                new Object[][]{
                        {
                                "With same instance",
                                Boolean.TRUE,
                                parsedArgument,
                                parsedArgument
                        },
                        {
                                "With null",
                                Boolean.FALSE,
                                new ParsedArgument(buildSchemeArgument("d", "debug")),
                                null
                        },
                        {
                                "With incompatible object",
                                Boolean.FALSE,
                                new ParsedArgument(buildSchemeArgument("d", "debug")),
                                ""
                        },
                        {
                                "With different options",
                                Boolean.FALSE,
                                new ParsedArgument(buildSchemeArgument("d", "debug")),
                                new ParsedArgument(buildSchemeArgument("h", "help"))
                        },
                        {
                                "With same options",
                                Boolean.TRUE,
                                new ParsedArgument(buildSchemeArgument("d", "debug")),
                                new ParsedArgument(buildSchemeArgument("d", "debug"))
                        },
                        {
                                "With different short options",
                                Boolean.FALSE,
                                new ParsedArgument(buildSchemeArgument("h", "debug")),
                                new ParsedArgument(buildSchemeArgument("d", "debug"))
                        },
                        {
                                "With different long options",
                                Boolean.FALSE,
                                new ParsedArgument(buildSchemeArgument("d", "debug")),
                                new ParsedArgument(buildSchemeArgument("d", "help"))
                        }
                }
        );
    }

    @Test
    public void equals() {
        if (shouldBeEqual()) {
            assertEqual();
            return;
        }

        assertNotEqual();
    }

    private Boolean shouldBeEqual() {
        return this.expected;
    }

    private void assertEqual() {
        assertTrue(this.message, this.parsedArgument.equals(this.compareTo));

        validateHashCodeWhenEqual();
    }

    private void validateHashCodeWhenEqual() {
        assertTrue(this.message, this.parsedArgument.hashCode() == this.compareTo.hashCode());
    }

    private void assertNotEqual() {
        assertFalse(this.message, this.parsedArgument.equals(this.compareTo));

        validateHashCodeWhenNotEqual();
    }

    private void validateHashCodeWhenNotEqual() {
        if (null == this.compareTo) {
            return;
        }

        assertFalse(this.message, this.parsedArgument.hashCode() == this.compareTo.hashCode());
    }
}
