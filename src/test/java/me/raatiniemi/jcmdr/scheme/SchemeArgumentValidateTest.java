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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SchemeArgumentValidateTest {
    private String message;
    private Boolean expected;
    private SchemeArgument schemeArgument;
    private String argument;

    public SchemeArgumentValidateTest(
            String message,
            Boolean expected,
            SchemeArgument schemeArgument,
            String argument
    ) {
        this.message = message;
        this.expected = expected;
        this.schemeArgument = schemeArgument;
        this.argument = argument;
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        SchemeArgument schemeArgument = new SchemeArgument.Builder()
                .shortName("d")
                .longName("debug")
                .build();

        return Arrays.asList(
                new Object[][]{
                        {
                                "Correct short name",
                                Boolean.TRUE,
                                schemeArgument,
                                "d"
                        },
                        {
                                "Incorrect short name",
                                Boolean.FALSE,
                                schemeArgument,
                                "v"
                        },
                        {
                                "Correct long name",
                                Boolean.TRUE,
                                schemeArgument,
                                "debug"
                        },
                        {
                                "Incorrect long name",
                                Boolean.FALSE,
                                schemeArgument,
                                "verbose"
                        },
                        {
                                "Correct long name (with different case)",
                                Boolean.TRUE,
                                schemeArgument,
                                "DEBUG"
                        }
                }
        );
    }

    @Test
    public void validate() {
        if (shouldValidationSucceed()) {
            assertSuccessfulValidation();
            return;
        }

        assertFailedValidation();
    }

    private Boolean shouldValidationSucceed() {
        return this.expected;
    }

    private void assertSuccessfulValidation() {
        assertTrue(this.message, performValidation());
    }

    private void assertFailedValidation() {
        assertFalse(this.message, performValidation());
    }

    private boolean performValidation() {
        return this.schemeArgument.validate(this.argument);
    }
}
