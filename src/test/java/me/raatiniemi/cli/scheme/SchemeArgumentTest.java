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

package me.raatiniemi.cli.scheme;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SchemeArgumentTest {
    private String message;
    private Boolean expected;
    private SchemeArgument schemeArgument;
    private Object compareTo;

    public SchemeArgumentTest(
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

    private static Method getMethodReference(String methodName)
            throws NoSuchMethodException {
        return SchemeArgumentTest.class.getMethod(methodName);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters()
            throws NoSuchMethodException {
        SchemeArgument schemeArgument = new SchemeArgument.Builder()
                .shortName("d")
                .build();

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
                                new SchemeArgument.Builder()
                                        .shortName("h")
                                        .build()
                        },
                        {
                                "With same short options",
                                Boolean.TRUE,
                                schemeArgument,
                                new SchemeArgument.Builder()
                                        .shortName("d")
                                        .build()
                        },
                        {
                                "With same long options",
                                Boolean.FALSE,
                                new SchemeArgument.Builder()
                                        .longName("help")
                                        .build(),
                                new SchemeArgument.Builder()
                                        .longName("debug")
                                        .build()
                        },
                        {
                                "With different long options",
                                Boolean.TRUE,
                                new SchemeArgument.Builder()
                                        .longName("debug")
                                        .build(),
                                new SchemeArgument.Builder()
                                        .longName("debug")
                                        .build()
                        },
                        {
                                "With same methods",
                                Boolean.TRUE,
                                new SchemeArgument.Builder()
                                        .methodReference(getMethodReference("equals"))
                                        .build(),
                                new SchemeArgument.Builder()
                                        .methodReference(getMethodReference("equals"))
                                        .build()
                        },
                        {
                                "With different methods",
                                Boolean.FALSE,
                                new SchemeArgument.Builder()
                                        .methodReference(getMethodReference("equals"))
                                        .build(),
                                new SchemeArgument.Builder()
                                        .methodReference(getMethodReference("hashCode"))
                                        .build()
                        },
                        {
                                "With null values",
                                Boolean.FALSE,
                                schemeArgument,
                                new SchemeArgument.Builder()
                                        .build()
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
        return this.expected;
    }

    private void assertEqual() {
        assertTrue(this.message, schemeArgument.equals(compareTo));

        validateHashCodeWhenEquals();
    }

    private void validateHashCodeWhenEquals() {
        assertTrue(this.message, schemeArgument.hashCode() == compareTo.hashCode());
    }

    private void assertNotEqual() {
        assertFalse(this.message, schemeArgument.equals(compareTo));

        validateHashCodeWhenNotEquals();
    }

    private void validateHashCodeWhenNotEquals() {
        if (null == compareTo) {
            return;
        }

        assertFalse(this.message, schemeArgument.hashCode() == compareTo.hashCode());
    }
}
