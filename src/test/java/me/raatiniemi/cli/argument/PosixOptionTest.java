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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class PosixOptionTest {
    private String message;
    private Boolean expected;
    private PosixOption option;
    private Object compareTo;

    public PosixOptionTest(
            String message,
            Boolean expected,
            PosixOption option,
            Object compareTo
    ) {
        this.message = message;
        this.expected = expected;
        this.option = option;
        this.compareTo = compareTo;
    }

    @Parameters
    public static Collection<Object[]> parameters() {
        PosixOption option = new PosixOption("d");

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
                                new PosixOption("h")
                        },
                        {
                                "With same option",
                                Boolean.TRUE,
                                option,
                                new PosixOption("d")
                        }
                }
        );
    }

    @Test
    public void equals() {
        if (shouldBeEqual()) {
            assertEqual();
            return;
        }

        assertNotEqual();
    }

    private Boolean shouldBeEqual() {
        return this.expected;
    }

    private void assertEqual() {
        assertTrue(this.message, this.option.equals(this.compareTo));

        validateHashCodeWhenEqual();
    }

    private void validateHashCodeWhenEqual() {
        assertTrue(this.message, this.option.hashCode() == this.compareTo.hashCode());
    }

    private void assertNotEqual() {
        assertFalse(this.message, this.option.equals(this.compareTo));

        validateHashCodeWhenNotEqual();
    }

    private void validateHashCodeWhenNotEqual() {
        if (null == this.compareTo) {
            return;
        }

        assertFalse(this.message, this.option.hashCode() == this.compareTo.hashCode());
    }
}
