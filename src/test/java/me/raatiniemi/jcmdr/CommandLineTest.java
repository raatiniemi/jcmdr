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

package me.raatiniemi.jcmdr;

import me.raatiniemi.jcmdr.scheme.annotation.Argument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CommandLineTest {
    private static final String METHOD_DEBUG = "debug";
    private static final String METHOD_VERBOSE = "verbose";

    private String message;
    private List<String> expected;
    private String[] arguments;

    public CommandLineTest(
            String message,
            String[] expected,
            String[] arguments
    ) {
        this.message = message;
        this.expected = Arrays.asList(expected);
        this.arguments = arguments;
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Enable debug with short name",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "-d"
                                }
                        },
                        {
                                "Enable debug with long name",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "--debug"
                                }
                        },
                        {
                                "Enable debug with short name java option",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "-Dd"
                                }
                        },
                        {
                                "Enable debug with long name java option",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "-Ddebug"
                                }
                        },
                        {
                                "Enable debug with short and long name",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "-d",
                                        "--debug",
                                        "-Dd",
                                        "-Ddebug"
                                }
                        },
                        {
                                "Enable debug with long name (uppercase)",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "--DEBUG"
                                }
                        },
                        {
                                "Enable debug with long name java option (uppercase)",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                new String[]{
                                        "-DDEBUG"
                                }
                        },
                        {
                                "Enable debug and verbose with short name",
                                new String[]{
                                        METHOD_DEBUG,
                                        METHOD_VERBOSE
                                },
                                new String[]{
                                        "-d",
                                        "-v"
                                }
                        },
                        {
                                "Enable debug and verbose with long name",
                                new String[]{
                                        METHOD_DEBUG,
                                        METHOD_VERBOSE
                                },
                                new String[]{
                                        "--debug",
                                        "--verbose"
                                }
                        },
                        {
                                "Enable debug and verbose with short name java options",
                                new String[]{
                                        METHOD_DEBUG,
                                        METHOD_VERBOSE
                                },
                                new String[]{
                                        "-Dd",
                                        "-Dv"
                                }
                        },
                        {
                                "Enable debug and verbose with long name java options",
                                new String[]{
                                        METHOD_DEBUG,
                                        METHOD_VERBOSE
                                },
                                new String[]{
                                        "-Ddebug",
                                        "-Dverbose"
                                }
                        },
                        {
                                "Argument with long name and value (separated with an equal sign)",
                                new String[]{
                                        "configuration-file=configuration.json"
                                },
                                new String[]{
                                        "--configuration-file=configuration.json"
                                }
                        },
                        {
                                "Argument with long name java option and value (separated with an equal sign)",
                                new String[]{
                                        "configuration-file=configuration.json"
                                },
                                new String[]{
                                        "-Dconfiguration-file=configuration.json"
                                }
                        }
                }
        );
    }

    @Test
    public void process() {
        ArgumentTarget argumentTarget = new ArgumentTarget();
        CommandLine.process(argumentTarget, this.arguments);

        assertEquals(this.message, this.expected, argumentTarget.actualCallStack);
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public class ArgumentTarget {
        List<String> actualCallStack = new ArrayList<>();

        @Argument(shortName = "d", longName = "debug")
        public void debug() {
            this.actualCallStack.add(METHOD_DEBUG);
        }

        @Argument(shortName = "v", longName = "verbose")
        public void verbose() {
            this.actualCallStack.add(METHOD_VERBOSE);
        }

        @Argument(longName = "configuration-file")
        public void setConfigurationFile(String filename) {
            this.actualCallStack.add("configuration-file=" + filename);
        }
    }
}
