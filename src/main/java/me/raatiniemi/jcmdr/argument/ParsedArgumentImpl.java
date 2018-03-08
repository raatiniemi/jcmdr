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

import me.raatiniemi.jcmdr.scheme.SchemeArgument;

import java.util.Objects;

final class ParsedArgumentImpl implements ParsedArgument {
    private final SchemeArgument schemeArgument;
    private final String argumentValue;

    private ParsedArgumentImpl(
            SchemeArgument schemeArgument,
            String argumentValue
    ) {
        this.schemeArgument = schemeArgument;
        this.argumentValue = argumentValue;
    }

    @Override
    public <T> void call(T target) {
        schemeArgument.call(target, argumentValue);
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

        if (!(o instanceof ParsedArgumentImpl)) {
            return false;
        }

        ParsedArgumentImpl that = (ParsedArgumentImpl) o;
        return Objects.equals(schemeArgument, that.schemeArgument)
                && Objects.equals(argumentValue, that.argumentValue);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Objects.hashCode(schemeArgument);
        result = 31 * result + Objects.hashCode(argumentValue);

        return result;
    }

    static class Builder {
        private SchemeArgument schemeArgument;
        private String argumentValue;

        Builder schemeArgument(SchemeArgument schemeArgument) {
            this.schemeArgument = schemeArgument;

            return this;
        }

        Builder argumentValue(String argumentValue) {
            this.argumentValue = argumentValue;

            return this;
        }

        ParsedArgument build() {
            return new ParsedArgumentImpl(schemeArgument, argumentValue);
        }
    }
}
