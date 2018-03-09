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

import me.raatiniemi.jcmdr.argument.exception.UnevenQuotesException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static me.raatiniemi.jcmdr.argument.ArgumentValueSanitizer.sanitize;

@RunWith(JUnit4.class)
public class ArgumentValueSanitizerTest {
    @Test(expected = UnevenQuotesException.class)
    public void sanitize_withOnlyDoubleQuotesAtBeginning() {
        String argumentValue = "\"foo";

        sanitize(argumentValue);
    }

    @Test(expected = UnevenQuotesException.class)
    public void sanitize_withOnlyDoubleQuotesAtEnd() {
        String argumentValue = "value\"";

        sanitize(argumentValue);
    }

    @Test(expected = UnevenQuotesException.class)
    public void sanitize_withOnlySingleQuotesAtBeginning() {
        String argumentValue = "'value";

        sanitize(argumentValue);
    }

    @Test(expected = UnevenQuotesException.class)
    public void sanitize_withOnlySingleQuotesAtEnd() {
        String argumentValue = "value'";

        sanitize(argumentValue);
    }
}
