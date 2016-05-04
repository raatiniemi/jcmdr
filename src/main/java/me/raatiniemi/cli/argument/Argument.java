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

import me.raatiniemi.cli.scheme.SchemeArgument;

class Argument {
    private SchemeArgument schemeArgument;

    Argument(SchemeArgument schemeArgument) {
        this.schemeArgument = schemeArgument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Argument)) {
            return false;
        }

        Argument that = (Argument) o;
        return this.schemeArgument.equals(that.schemeArgument);
    }

    @Override
    public int hashCode() {
        return this.schemeArgument.hashCode();
    }
}
