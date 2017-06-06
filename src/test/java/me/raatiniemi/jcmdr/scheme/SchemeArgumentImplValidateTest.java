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

import static java.util.Objects.isNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SchemeArgumentImplValidateTest {
    private String message;
    private Boolean expected;
    private SchemeArgument schemeArgument;
    private String argument;
    private Class<?>[] argumentValueTypes;

    public SchemeArgumentImplValidateTest(
            String message,
            Boolean expected,
            SchemeArgument schemeArgument,
            String argument,
            Class<?>[] argumentValueTypes
    ) {
        this.message = message;
        this.expected = expected;
        this.schemeArgument = schemeArgument;
        this.argument = argument;
        this.argumentValueTypes = argumentValueTypes;
    }

    @Parameters
    public static Collection<Object[]> parameters() throws NoSuchMethodException {
        SchemeArgument schemeArgument = SchemeArgumentBuilder.build("d", "debug");

        return Arrays.asList(
                new Object[][]{
                        {
                                "Correct short name",
                                Boolean.TRUE,
                                schemeArgument,
                                "d",
                                null
                        },
                        {
                                "Incorrect short name",
                                Boolean.FALSE,
                                schemeArgument,
                                "v",
                                null
                        },
                        {
                                "Correct long name",
                                Boolean.TRUE,
                                schemeArgument,
                                "debug",
                                null
                        },
                        {
                                "Incorrect long name",
                                Boolean.FALSE,
                                schemeArgument,
                                "verbose",
                                null
                        },
                        {
                                "Correct long name (with different case)",
                                Boolean.TRUE,
                                schemeArgument,
                                "DEBUG",
                                null
                        },
                        {
                                "Invalid type for argument value",
                                Boolean.FALSE,
                                SchemeArgumentBuilder.buildWithShortName(
                                        "d",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArgument",
                                                String.class
                                        )
                                ),
                                "d",
                                new Class[]{
                                        Object.class
                                }
                        },
                        {
                                "Valid type for argument value",
                                Boolean.TRUE,
                                SchemeArgumentBuilder.buildWithShortName(
                                        "d",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArgument",
                                                String.class
                                        )
                                ),
                                "d",
                                new Class[]{
                                        String.class
                                }
                        },
                        {
                                "Invalid type for argument values",
                                Boolean.FALSE,
                                SchemeArgumentBuilder.buildWithShortName(
                                        "d",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArguments",
                                                String.class,
                                                Long.class
                                        )
                                ),
                                "d",
                                new Class[]{
                                        String.class,
                                        Object.class
                                }
                        },
                        {
                                "Valid type for argument values",
                                Boolean.TRUE,
                                SchemeArgumentBuilder.buildWithShortName(
                                        "d",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArguments",
                                                String.class,
                                                Long.class
                                        )
                                ),
                                "d",
                                new Class[]{
                                        String.class,
                                        Long.class
                                }
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
        if (isMissingArgumentValues()) {
            return validateWithoutArgumentValues();
        }

        return validateWithArgumentValues();
    }

    private boolean isMissingArgumentValues() {
        return isNull(argumentValueTypes);
    }

    private boolean validateWithoutArgumentValues() {
        return this.schemeArgument.validate(this.argument);
    }

    private boolean validateWithArgumentValues() {
        return this.schemeArgument.validate(this.argument, this.argumentValueTypes);
    }
}
