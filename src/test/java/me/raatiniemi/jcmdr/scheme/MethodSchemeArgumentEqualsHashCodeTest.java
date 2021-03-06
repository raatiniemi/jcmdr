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
public class MethodSchemeArgumentEqualsHashCodeTest {
    private String message;
    private Boolean expected;
    private SchemeArgument schemeArgument;
    private Object compareTo;

    public MethodSchemeArgumentEqualsHashCodeTest(
            String message,
            Boolean expected,
            SchemeArgument schemeArgument,
            Object compareTo
    ) {
        this.message = message;
        this.expected = expected;
        this.schemeArgument = schemeArgument;
        this.compareTo = compareTo;
    }

    @Parameters
    public static Collection<Object[]> parameters() throws NoSuchMethodException {
        SchemeArgument schemeArgument = SchemeArgumentBuilder.buildWithShortName("d");

        return Arrays.asList(
                new Object[][]{
                        {
                                "With same instance",
                                Boolean.TRUE,
                                schemeArgument,
                                schemeArgument
                        },
                        {
                                "With null",
                                Boolean.FALSE,
                                schemeArgument,
                                null
                        },
                        {
                                "With incompatible object",
                                Boolean.FALSE,
                                schemeArgument,
                                ""
                        },
                        {
                                "With different short options",
                                Boolean.FALSE,
                                schemeArgument,
                                SchemeArgumentBuilder.buildWithShortName("h")
                        },
                        {
                                "With same short options",
                                Boolean.TRUE,
                                schemeArgument,
                                SchemeArgumentBuilder.buildWithShortName("d")
                        },
                        {
                                "With same short options (different case)",
                                Boolean.FALSE,
                                schemeArgument,
                                SchemeArgumentBuilder.buildWithShortName("D")
                        },
                        {
                                "With different long options",
                                Boolean.FALSE,
                                SchemeArgumentBuilder.buildWithLongName("help"),
                                SchemeArgumentBuilder.buildWithLongName("debug")
                        },
                        {
                                "With same long options",
                                Boolean.TRUE,
                                SchemeArgumentBuilder.buildWithLongName("debug"),
                                SchemeArgumentBuilder.buildWithLongName("debug")
                        },
                        {
                                "With same long options (different case)",
                                Boolean.TRUE,
                                SchemeArgumentBuilder.buildWithLongName("debug"),
                                SchemeArgumentBuilder.buildWithLongName("DEBUG")
                        },
                        {
                                "With same methods",
                                Boolean.TRUE,
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "firstMethodWithoutArguments"
                                        )
                                ),
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "firstMethodWithoutArguments"
                                        )
                                )
                        },
                        {
                                "With different methods",
                                Boolean.FALSE,
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "firstMethodWithoutArguments"
                                        )
                                ),
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "secondMethodWithoutArguments"
                                        )
                                )
                        },
                        {
                                "With same methods (different argument types)",
                                Boolean.FALSE,
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArgument",
                                                String.class
                                        )
                                ),
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArgument",
                                                Long.class
                                        )
                                )
                        },
                        {
                                "With same methods (same argument types in different order)",
                                Boolean.FALSE,
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArguments",
                                                String.class,
                                                Long.class
                                        )
                                ),
                                SchemeArgumentBuilder.build(
                                        "d",
                                        "debug",
                                        SchemeArgumentImplTestReference.getMethodReference(
                                                "methodWithArguments",
                                                Long.class,
                                                String.class
                                        )
                                )
                        }
                }
        );
    }

    @Test
    public void equals() {
        if (shouldBeEquals()) {
            assertEqual();
            return;
        }

        assertNotEqual();
    }

    private Boolean shouldBeEquals() {
        return expected;
    }

    private void assertEqual() {
        assertTrue(message, schemeArgument.equals(compareTo));

        validateHashCodeWhenEquals();
    }

    private void validateHashCodeWhenEquals() {
        assertTrue(message, schemeArgument.hashCode() == compareTo.hashCode());
    }

    private void assertNotEqual() {
        assertFalse(message, schemeArgument.equals(compareTo));

        validateHashCodeWhenNotEquals();
    }

    private void validateHashCodeWhenNotEquals() {
        if (isNull(compareTo)) {
            return;
        }

        assertFalse(message, schemeArgument.hashCode() == compareTo.hashCode());
    }
}
