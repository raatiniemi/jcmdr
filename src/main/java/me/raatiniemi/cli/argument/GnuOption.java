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

class GnuOption extends Argument {
    private String option;

    GnuOption(String option) {
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof GnuOption)) {
            return false;
        }

        GnuOption that = (GnuOption) o;
        return this.option.equals(that.option);

    }

    @Override
    public int hashCode() {
        return this.option.hashCode();
    }
}
