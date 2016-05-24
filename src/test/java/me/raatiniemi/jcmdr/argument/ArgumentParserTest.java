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

import me.raatiniemi.jcmdr.scheme.SchemeArgument;
import me.raatiniemi.jcmdr.scheme.SchemeArgumentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ArgumentParserTest {
    private String message;
    private Collection<ParsedArgument> expected;
    private ArgumentParser parser;

    public ArgumentParserTest(
            String message,
            ParsedArgument[] expected,
            String arguments,
            SchemeArgument[] schemeArguments
    ) {
        this.message = message;
        if (null != expected) {
            this.expected = new LinkedHashSet<>(Arrays.asList(expected));
        }

        this.parser = new ArgumentParser(
                arguments,
                null == schemeArguments ? null : Arrays.asList(schemeArguments)
        );
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
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "With null arguments",
                                null,
                                null,
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "Without argument scheme",
                                null,
                                "-d",
                                null
                        },
                        {
                                "With empty argument scheme",
                                null,
                                "-d",
                                new SchemeArgument[]{}
                        },
                        {
                                "With short option",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                },
                                "-d",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "With short options",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("d")),
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("v"))
                                },
                                "-d -v",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d"),
                                        SchemeArgumentBuilder.buildWithShortName("v")
                                }
                        },
                        {
                                "With short options (combined)",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("d")),
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("v"))
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d"),
                                        SchemeArgumentBuilder.buildWithShortName("v")
                                }
                        },
                        {
                                "With same short options (different cases)",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("V")),
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("v"))
                                },
                                "-V -v",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("V"),
                                        SchemeArgumentBuilder.buildWithShortName("v")
                                }
                        },
                        {
                                "With short options, without full argument scheme",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "With long option",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                },
                                "--debug",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "With long options",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("debug")),
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("verbose"))
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug"),
                                        SchemeArgumentBuilder.buildWithLongName("verbose")
                                }
                        },
                        {
                                "With long option (different case)",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                },
                                "--DEBUG",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "With long options, without full argument scheme",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "Long option separated with dash",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("human-readable"))
                                },
                                "--human-readable",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("human-readable")
                                }
                        },
                        {
                                "With short and long options",
                                new ParsedArgument[]{
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithShortName("d")),
                                        new ParsedArgument(SchemeArgumentBuilder.buildWithLongName("verbose"))
                                },
                                "-d --verbose",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d"),
                                        SchemeArgumentBuilder.buildWithLongName("verbose")
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
        Collection<ParsedArgument> actual = this.parser.parse();
        assertEquals(this.message, this.expected, actual);
    }

    private void assertInvalidArguments() {
        Collection<ParsedArgument> actual = this.parser.parse();
        assertTrue(this.message, actual.isEmpty());
    }
}
