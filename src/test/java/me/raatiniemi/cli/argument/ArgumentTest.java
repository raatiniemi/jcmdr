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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

abstract class ArgumentTest {
    private String message;
    private Boolean expected;
    private Argument argument;
    private Object compareTo;

    ArgumentTest(
            String message,
            Boolean expected,
            Argument argument,
            Object compareTo
    ) {
        this.compareTo = compareTo;
        this.expected = expected;
        this.message = message;
        this.argument = argument;
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
        assertTrue(this.message, this.argument.equals(this.compareTo));

        validateHashCodeWhenEqual();
    }

    private void validateHashCodeWhenEqual() {
        assertTrue(this.message, this.argument.hashCode() == this.compareTo.hashCode());
    }

    private void assertNotEqual() {
        assertFalse(this.message, this.argument.equals(this.compareTo));

        validateHashCodeWhenNotEqual();
    }

    private void validateHashCodeWhenNotEqual() {
        if (null == this.compareTo) {
            return;
        }

        assertFalse(this.message, this.argument.hashCode() == this.compareTo.hashCode());
    }
}
