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

import me.raatiniemi.jcmdr.scheme.SchemeArgument;
import me.raatiniemi.jcmdr.scheme.SchemeArgumentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
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
        if (nonNull(expected)) {
            this.expected = new LinkedHashSet<>(Arrays.asList(expected));
        }

        if (isNull(schemeArguments)) {
            parser = new ArgumentParser(arguments, null);
            return;
        }

        parser = new ArgumentParser(arguments, Arrays.asList(schemeArguments));
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
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build()
                                },
                                "-d",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "With short options",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build(),
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("v"))
                                                .build()
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
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build(),
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("v"))
                                                .build()
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
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("V"))
                                                .build(),
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("v"))
                                                .build()
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
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build()
                                },
                                "-dv",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "With long option",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                                .build()
                                },
                                "--debug",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "With long options",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                                .build(),
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("verbose"))
                                                .build()
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
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                                .build()
                                },
                                "--DEBUG",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "With long options, without full argument scheme",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                                .build()
                                },
                                "--debug --verbose",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "Long option separated with dash",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("human-readable"))
                                                .build()
                                },
                                "--human-readable",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("human-readable")
                                }
                        },
                        {
                                "With short and long options",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build(),
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("verbose"))
                                                .build()
                                },
                                "-d --verbose",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d"),
                                        SchemeArgumentBuilder.buildWithLongName("verbose")
                                }
                        },
                        {
                                "With short name java option",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build()
                                },
                                "-Dd",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d")
                                }
                        },
                        {
                                "With long name java option",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("debug"))
                                                .build()
                                },
                                "-Ddebug",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("debug")
                                }
                        },
                        {
                                "With short and long name java options",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithShortName("d"))
                                                .build(),
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("verbose"))
                                                .build()
                                },
                                "-Dd -Dverbose",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithShortName("d"),
                                        SchemeArgumentBuilder.buildWithLongName("verbose")
                                }
                        },
                        {
                                "With uppercase long name java option (separated with -D)",
                                new ParsedArgument[]{
                                        new ParsedArgumentImpl.Builder()
                                                .schemeArgument(SchemeArgumentBuilder.buildWithLongName("ENABLE-DEBUG"))
                                                .build()
                                },
                                "-DENABLE-DEBUG",
                                new SchemeArgument[]{
                                        SchemeArgumentBuilder.buildWithLongName("ENABLE-DEBUG")
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
        return nonNull(expected);
    }

    private void assertValidArguments() {
        Collection<ParsedArgument> actual = parser.parse();
        assertEquals(message, expected, actual);
    }

    private void assertInvalidArguments() {
        Collection<ParsedArgument> actual = parser.parse();
        assertTrue(message, actual.isEmpty());
    }
}
