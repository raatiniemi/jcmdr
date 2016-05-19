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

import me.raatiniemi.jcmdr.exception.InvokeArgumentException;
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
    private String arguments;

    public CommandLineTest(
            String message,
            String[] expected,
            String arguments
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
                                "-d"
                        },
                        {
                                "Enable debug with long name",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                "--debug"
                        },
                        {
                                "Enable debug with short and long name",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                "-d --debug"
                        },
                        {
                                "Enable debug with long name (uppercase)",
                                new String[]{
                                        METHOD_DEBUG
                                },
                                "--DEBUG"
                        },
                        {
                                "Enable debug and verbose with short name",
                                new String[]{
                                        METHOD_DEBUG,
                                        METHOD_VERBOSE
                                },
                                "-d -v"
                        },
                        {
                                "Enable debug and verbose with long name",
                                new String[]{
                                        METHOD_DEBUG,
                                        METHOD_VERBOSE
                                },
                                "--debug --verbose"
                        }
                }
        );
    }

    @Test
    public void process() throws InvokeArgumentException {
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
    }
}
