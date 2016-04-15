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

import java.util.Objects;

class SchemeArgument {
    private final String shortName;

    private SchemeArgument(String shortName) {
        this.shortName = shortName;
    }

    private String getShortName() {
        return this.shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SchemeArgument)) {
            return false;
        }

        SchemeArgument argument = (SchemeArgument) o;
        return Objects.equals(getShortName(), argument.getShortName());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Objects.hashCode(getShortName());

        return result;
    }

    static class Builder {
        private String shortName;

        Builder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        SchemeArgument build() {
            return new SchemeArgument(this.shortName);
        }
    }
}
