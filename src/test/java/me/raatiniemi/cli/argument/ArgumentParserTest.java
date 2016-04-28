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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ArgumentParserTest {
    private String message;
    private List<Argument> expected;
    private ArgumentParser parser;

    public ArgumentParserTest(
            String message,
            Argument[] expected,
            String arguments,
            SchemeArgument[] schemeArguments
    ) {
        this.message = message;
        if (null != expected) {
            this.expected = Arrays.asList(expected);
        }

        this.parser = new ArgumentParser(arguments, schemeArguments);
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
        return Arrays.asList(
                new Object[][]{
                        {
                                "Without arguments",
                                null,
                                "",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null)
                                }
                        },
                        {
                                "With null arguments",
                                null,
                                null,
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null)
                                }
                        },
                        {
                                "Without argument scheme",
                                null,
                                "-d",
                                null
                        },
                        {
                                "With POSIX option",
                                new Argument[]{
                                        new PosixOption("d")
                                },
                                "-d",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null)
                                }
                        },
                        {
                                "With POSIX options",
                                new PosixOption[]{
                                        new PosixOption("d"),
                                        new PosixOption("v")
                                },
                                "-d -v",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null),
                                        buildSchemeArgument("v", null)
                                }
                        },
                        {
                                "With POSIX options (combined)",
                                new Argument[]{
                                        new PosixOption("d"),
                                        new PosixOption("v")
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null),
                                        buildSchemeArgument("v", null)
                                }
                        },
                        {
                                "With POSIX options and without full argument scheme",
                                new Argument[]{
                                        new PosixOption("d")
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null)
                                }
                        },
                        {
                                "With GNU option",
                                new Argument[]{
                                        new GnuOption("debug")
                                },
                                "--debug",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "debug")
                                }
                        },
                        {
                                "With GNU options",
                                new Argument[]{
                                        new GnuOption("debug"),
                                        new GnuOption("verbose")
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "debug"),
                                        buildSchemeArgument(null, "verbose")
                                }
                        },
                        {
                                "With GNU options and without full argument scheme",
                                new Argument[]{
                                        new GnuOption("debug")
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        buildSchemeArgument("", "debug")
                                }
                        },
                        {
                                "GNU option separated with dash",
                                new Argument[]{
                                        new GnuOption("human-readable")
                                },
                                "--human-readable",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "human-readable")
                                }
                        },
                        {
                                "With POSIX and GNU options",
                                new Argument[]{
                                        new PosixOption("d"),
                                        new GnuOption("verbose")
                                },
                                "-d --verbose",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null),
                                        buildSchemeArgument(null, "verbose")
                                }
                        }
                }
        );
    }

    @Test
    public void parse() {
        if (haveValidArguments()) {
            assertValidArguments();
            return;
        }

        assertInvalidArguments();
    }

    private boolean haveValidArguments() {
        return null != this.expected;
    }

    private void assertValidArguments() {
        List<Argument> actual = this.parser.parse();
        assertEquals(this.message, this.expected, actual);
    }

    private void assertInvalidArguments() {
        List<Argument> actual = this.parser.parse();
        assertTrue(this.message, actual.isEmpty());
    }
}
