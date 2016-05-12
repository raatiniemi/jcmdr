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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SchemeParserTest {
    private String message;
    private List<SchemeArgument> expected;

    private SchemeParser parser;

    public SchemeParserTest(String message, SchemeArgument[] expected, Class<?> parseTarget) {
        this.message = message;
        if (null != expected) {
            this.expected = Arrays.asList(expected);
        }

        this.parser = new SchemeParser(parseTarget);
    }

    @Parameters
    public static Collection<Object[]> parameters()
            throws NoSuchMethodException {
        return Arrays.asList(
                new Object[][]{
                        {
                                "With null argument",
                                null,
                                WithoutScheme.class
                        },
                        {
                                "With short name option",
                                new SchemeArgument[]{
                                        new SchemeArgument.Builder()
                                                .shortName("d")
                                                .methodReference(WithShortNameOption.class.getMethod("d"))
                                                .build()
                                },
                                WithShortNameOption.class
                        },
                        {
                                "With short name options",
                                new SchemeArgument[]{
                                        new SchemeArgument.Builder()
                                                .shortName("d")
                                                .methodReference(WithShortNameOptions.class.getMethod("d"))
                                                .build(),
                                        new SchemeArgument.Builder()
                                                .shortName("h")
                                                .methodReference(WithShortNameOptions.class.getMethod("h"))
                                                .build()
                                },
                                WithShortNameOptions.class
                        },
                        {
                                "With long name option",
                                new SchemeArgument[]{
                                        new SchemeArgument.Builder()
                                                .longName("debug")
                                                .methodReference(WithLongNameOption.class.getMethod("d"))
                                                .build()
                                },
                                WithLongNameOption.class
                        },
                        {
                                "With long name options",
                                new SchemeArgument[]{
                                        new SchemeArgument.Builder()
                                                .longName("debug")
                                                .methodReference(WithLongNameOptions.class.getMethod("d"))
                                                .build(),
                                        new SchemeArgument.Builder()
                                                .longName("help")
                                                .methodReference(WithLongNameOptions.class.getMethod("h"))
                                                .build()
                                },
                                WithLongNameOptions.class
                        },
                        {
                                "With short and long name options",
                                new SchemeArgument[]{
                                        new SchemeArgument.Builder()
                                                .shortName("d")
                                                .longName("debug")
                                                .methodReference(WithShortAndLongNameOptions.class.getMethod("d"))
                                                .build(),
                                        new SchemeArgument.Builder()
                                                .shortName("h")
                                                .longName("help")
                                                .methodReference(WithShortAndLongNameOptions.class.getMethod("h"))
                                                .build()
                                },
                                WithShortAndLongNameOptions.class
                        }
                }
        );
    }

    @Test
    public void parse() {
        if (haveValidArgumentScheme()) {
            assertValidArgumentScheme();
            return;
        }

        assertInvalidScheme();
    }

    private boolean haveValidArgumentScheme() {
        return null != this.expected;
    }

    private void assertValidArgumentScheme() {
        List<SchemeArgument> actual = this.parser.parse();
        assertEquals(this.message, this.expected, actual);
    }

    private void assertInvalidScheme() {
        List<SchemeArgument> actual = this.parser.parse();
        assertTrue(this.message, actual.isEmpty());
    }

    @SuppressWarnings("unused")
    private class WithoutScheme {
        public void unavailableMethod() {
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private class WithShortNameOption {
        @Argument(shortName = "d")
        public void d() {
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private class WithShortNameOptions {
        @Argument(shortName = "d")
        public void d() {
        }

        @Argument(shortName = "h")
        public void h() {
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private class WithLongNameOption {
        @Argument(longName = "debug")
        public void d() {
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private class WithLongNameOptions {
        @Argument(longName = "debug")
        public void d() {
        }

        @Argument(longName = "help")
        public void h() {
        }
    }

    @SuppressWarnings({"unused", "WeakerAccess"})
    private class WithShortAndLongNameOptions {
        @Argument(shortName = "d", longName = "debug")
        public void d() {
        }

        @Argument(shortName = "h", longName = "help")
        public void h() {
        }
    }
}
