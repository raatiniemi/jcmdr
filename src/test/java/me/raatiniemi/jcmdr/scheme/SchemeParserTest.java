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

package me.raatiniemi.jcmdr.scheme;

import me.raatiniemi.jcmdr.scheme.annotation.Argument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Method;
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

    private static Method getMethodReference(
            Class<?> classReference,
            String methodName,
            Class... arguments
    ) throws NoSuchMethodException {
        return classReference.getMethod(methodName, arguments);
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
                                        SchemeArgumentBuilder.buildWithShortName(
                                                "d",
                                                getMethodReference(
                                                        WithShortNameOption.class,
                                                        "d"
                                                )
                                        )
                                },
                                WithShortNameOption.class
                        },
                        {
                                "With short name options",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName(
                                                "d",
                                                getMethodReference(
                                                        WithShortNameOptions.class,
                                                        "d"
                                                )
                                        ),
                                        SchemeArgumentBuilder.buildWithShortName(
                                                "h",
                                                getMethodReference(
                                                        WithShortNameOptions.class,
                                                        "h"
                                                )
                                        )
                                },
                                WithShortNameOptions.class
                        },
                        {
                                "With long name option",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName(
                                                "debug",
                                                getMethodReference(
                                                        WithLongNameOption.class,
                                                        "d"
                                                )
                                        )
                                },
                                WithLongNameOption.class
                        },
                        {
                                "With long name options",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName(
                                                "debug",
                                                getMethodReference(
                                                        WithLongNameOptions.class,
                                                        "d"
                                                )
                                        ),
                                        SchemeArgumentBuilder.buildWithLongName(
                                                "help",
                                                getMethodReference(
                                                        WithLongNameOptions.class,
                                                        "h"
                                                )
                                        )
                                },
                                WithLongNameOptions.class
                        },
                        {
                                "With short and long name options",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.build(
                                                "d",
                                                "debug",
                                                getMethodReference(
                                                        WithShortAndLongNameOptions.class,
                                                        "d"
                                                )
                                        ),
                                        SchemeArgumentBuilder.build(
                                                "h",
                                                "help",
                                                getMethodReference(
                                                        WithShortAndLongNameOptions.class,
                                                        "h"
                                                )
                                        )
                                },
                                WithShortAndLongNameOptions.class
                        },
                        {
                                "With short name argument",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName(
                                                "f",
                                                getMethodReference(
                                                        WithShortNameArgument.class,
                                                        "f",
                                                        String.class
                                                )
                                        )
                                },
                                WithShortNameArgument.class
                        },
                        {
                                "With long name argument",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName(
                                                "file",
                                                getMethodReference(
                                                        WithLongNameArgument.class,
                                                        "f",
                                                        String.class
                                                )
                                        )
                                },
                                WithLongNameArgument.class
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

    @SuppressWarnings({"unused"})
    private class WithShortNameArgument {
        @Argument(shortName = "f")
        public void f(String filename) {
        }
    }

    @SuppressWarnings({"unused"})
    private class WithLongNameArgument {
        @Argument(longName = "file")
        public void f(String filename) {
        }
    }
}
