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

package me.raatiniemi.jcmdr.argument;

import me.raatiniemi.jcmdr.exception.InvokeArgumentException;
import me.raatiniemi.jcmdr.scheme.SchemeArgument;

public class ParsedArgument {
    private SchemeArgument schemeArgument;

    private ParsedArgument(SchemeArgument schemeArgument) {
        this.schemeArgument = schemeArgument;
    }

    public <T> void call(T target) throws InvokeArgumentException {
        this.schemeArgument.call(target);
    }

    @Override
    public String toString() {
        return schemeArgument.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ParsedArgument)) {
            return false;
        }

        ParsedArgument that = (ParsedArgument) o;
        return this.schemeArgument.equals(that.schemeArgument);
    }

    @Override
    public int hashCode() {
        return this.schemeArgument.hashCode();
    }

    static class Builder {
        private SchemeArgument schemeArgument;

        Builder schemeArgument(SchemeArgument schemeArgument) {
            this.schemeArgument = schemeArgument;

            return this;
        }

        ParsedArgument build() {
            return new ParsedArgument(this.schemeArgument);
        }
    }
}
