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

package me.raatiniemi.jcmdr.argument;

import me.raatiniemi.jcmdr.scheme.SchemeArgumentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ParsedArgumentImplTest {
    private String message;
    private Boolean expected;
    private ParsedArgument parsedArgument;
    private Object compareTo;

    public ParsedArgumentImplTest(
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

    @Parameters
    public static Collection<Object[]> parameters() {
        ParsedArgument parsedArgument = new ParsedArgumentImpl.Builder()
                .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                .build();

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
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build(),
                                null
                        },
                        {
                                "With incompatible object",
                                Boolean.FALSE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build(),
                                ""
                        },
                        {
                                "With different options",
                                Boolean.FALSE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build(),
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("h", "help"))
                                        .build()
                        },
                        {
                                "With same options",
                                Boolean.TRUE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build(),
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build()
                        },
                        {
                                "With different short options",
                                Boolean.FALSE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("h", "debug"))
                                        .build(),
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build()
                        },
                        {
                                "With different long options",
                                Boolean.FALSE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .build(),
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "help"))
                                        .build()
                        },
                        {
                                "With same argument value",
                                Boolean.TRUE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .argumentValue("verbose")
                                        .build(),
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .argumentValue("verbose")
                                        .build()
                        },
                        {
                                "With different argument value",
                                Boolean.FALSE,
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .argumentValue("verbose")
                                        .build(),
                                new ParsedArgumentImpl.Builder()
                                        .schemeArgument(SchemeArgumentBuilder.build("d", "debug"))
                                        .argumentValue("debug")
                                        .build()
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
