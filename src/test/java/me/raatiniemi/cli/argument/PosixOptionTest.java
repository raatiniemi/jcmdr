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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PosixOptionTest extends ArgumentTest {
    public PosixOptionTest(
            String message,
            Boolean expected,
            PosixOption option,
            Object compareTo
    ) {
        super(message, expected, option, compareTo);
    }

    private static SchemeArgument buildSchemeArgument(String shortName) {
        return new SchemeArgument.Builder()
                .shortName(shortName)
                .build();
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        PosixOption option = new PosixOption(buildSchemeArgument("d"));

        return Arrays.asList(
                new Object[][]{
                        {
                                "With same instance",
                                Boolean.TRUE,
                                option,
                                option
                        },
                        {
                                "With null",
                                Boolean.FALSE,
                                option,
                                null
                        },
                        {
                                "With incompatible object",
                                Boolean.FALSE,
                                option,
                                ""
                        },
                        {
                                "With different option",
                                Boolean.FALSE,
                                option,
                                new PosixOption(buildSchemeArgument("h"))
                        },
                        {
                                "With same option",
                                Boolean.TRUE,
                                option,
                                new PosixOption(buildSchemeArgument("d"))
                        }
                }
        );
    }
}
