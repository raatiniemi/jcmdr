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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ArgumentTest {
    private String message;
    private Boolean expected;
    private Argument argument;
    private Object compareTo;

    public ArgumentTest(
            String message,
            Boolean expected,
            Argument argument,
            Object compareTo
    ) {
        this.compareTo = compareTo;
        this.expected = expected;
        this.message = message;
        this.argument = argument;
    }

    private static SchemeArgument buildSchemeArgument(
            String shortName,
            String longName
    ) {
        return new SchemeArgument.Builder()
                .shortName(shortName)
                .longName(longName)
                .build();
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        Argument argument = new Argument(buildSchemeArgument("d", "debug"));

        return Arrays.asList(
                new Object[][]{
                        {
                                "With same instance",
                                Boolean.TRUE,
                                argument,
                                argument
                        },
                        {
                                "With null",
                                Boolean.FALSE,
                                new Argument(buildSchemeArgument("d", "debug")),
                                null
                        },
                        {
                                "With incompatible object",
                                Boolean.FALSE,
                                new Argument(buildSchemeArgument("d", "debug")),
                                ""
                        },
                        {
                                "With different options",
                                Boolean.FALSE,
                                new Argument(buildSchemeArgument("d", "debug")),
                                new Argument(buildSchemeArgument("h", "help"))
                        },
                        {
                                "With same options",
                                Boolean.TRUE,
                                new Argument(buildSchemeArgument("d", "debug")),
                                new Argument(buildSchemeArgument("d", "debug"))
                        },
                        {
                                "With different short options",
                                Boolean.FALSE,
                                new Argument(buildSchemeArgument("h", "debug")),
                                new Argument(buildSchemeArgument("d", "debug"))
                        },
                        {
                                "With different long options",
                                Boolean.FALSE,
                                new Argument(buildSchemeArgument("d", "debug")),
                                new Argument(buildSchemeArgument("d", "help"))
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
        assertTrue(this.message, this.argument.equals(this.compareTo));

        validateHashCodeWhenEqual();
    }

    private void validateHashCodeWhenEqual() {
        assertTrue(this.message, this.argument.hashCode() == this.compareTo.hashCode());
    }

    private void assertNotEqual() {
        assertFalse(this.message, this.argument.equals(this.compareTo));

        validateHashCodeWhenNotEqual();
    }

    private void validateHashCodeWhenNotEqual() {
        if (null == this.compareTo) {
            return;
        }

        assertFalse(this.message, this.argument.hashCode() == this.compareTo.hashCode());
    }
}
