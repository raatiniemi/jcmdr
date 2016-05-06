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
    private List<ParsedArgument> expected;
    private ArgumentParser parser;

    public ArgumentParserTest(
            String message,
            ParsedArgument[] expected,
            String arguments,
            SchemeArgument[] schemeArguments
    ) {
        this.message = message;
        if (null != expected) {
            this.expected = Arrays.asList(expected);
        }

        this.parser = new ArgumentParser(
                arguments,
                null == schemeArguments ? null : Arrays.asList(schemeArguments)
        );
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
                                "With short option",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument("d", null))
                                },
                                "-d",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null)
                                }
                        },
                        {
                                "With short options",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument("d", null)),
                                        new ParsedArgument(buildSchemeArgument("v", null))
                                },
                                "-d -v",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null),
                                        buildSchemeArgument("v", null)
                                }
                        },
                        {
                                "With short options (combined)",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument("d", null)),
                                        new ParsedArgument(buildSchemeArgument("v", null))
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null),
                                        buildSchemeArgument("v", null)
                                }
                        },
                        {
                                "With short options, without full argument scheme",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument("d", null))
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        buildSchemeArgument("d", null)
                                }
                        },
                        {
                                "With long option",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument(null, "debug"))
                                },
                                "--debug",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "debug")
                                }
                        },
                        {
                                "With long options",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument(null, "debug")),
                                        new ParsedArgument(buildSchemeArgument(null, "verbose"))
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "debug"),
                                        buildSchemeArgument(null, "verbose")
                                }
                        },
                        {
                                "With long options, without full argument scheme",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument(null, "debug"))
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "debug")
                                }
                        },
                        {
                                "Long option separated with dash",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument(null, "human-readable"))
                                },
                                "--human-readable",
                                new SchemeArgument[]{
                                        buildSchemeArgument(null, "human-readable")
                                }
                        },
                        {
                                "With short and long options",
                                new ParsedArgument[]{
                                        new ParsedArgument(buildSchemeArgument("d", null)),
                                        new ParsedArgument(buildSchemeArgument(null, "verbose"))
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
        List<ParsedArgument> actual = this.parser.parse();
        assertEquals(this.message, this.expected, actual);
    }

    private void assertInvalidArguments() {
        List<ParsedArgument> actual = this.parser.parse();
        assertTrue(this.message, actual.isEmpty());
    }
}
