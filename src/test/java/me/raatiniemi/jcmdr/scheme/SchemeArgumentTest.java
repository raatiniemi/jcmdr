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

import me.raatiniemi.jcmdr.scheme.exception.InvalidLongNameException;
import me.raatiniemi.jcmdr.scheme.exception.InvalidShortNameException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SchemeArgumentTest {
    @Test(expected = InvalidShortNameException.class)
    public void build_withInvalidShortName() {
        SchemeArgumentBuilder.buildWithShortName("debug");
    }

    @Test(expected = InvalidLongNameException.class)
    public void build_withInvalidLongName() {
        SchemeArgumentBuilder.buildWithLongName("d");
    }
}
