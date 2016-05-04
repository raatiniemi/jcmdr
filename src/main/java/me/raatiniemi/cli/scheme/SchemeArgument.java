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

import java.lang.reflect.Method;
import java.util.Objects;

public class SchemeArgument {
    private final String shortName;
    private final String longName;
    private final Method methodReference;

    private SchemeArgument(
            String shortName,
            String longName,
            Method methodReference
    ) {
        this.shortName = shortName;
        this.longName = longName;
        this.methodReference = methodReference;
    }

    private String getShortName() {
        return this.shortName;
    }

    private String getLongName() {
        return longName;
    }

    public boolean validate(String argument) {
        return argument.equals(getShortName())
                || argument.equals(getLongName());
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
        return Objects.equals(getShortName(), argument.getShortName())
                && Objects.equals(getLongName(), argument.getLongName())
                && Objects.equals(this.methodReference, argument.methodReference);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Objects.hashCode(getShortName());
        result = 31 * result + Objects.hashCode(getLongName());
        result = 31 * result + Objects.hashCode(this.methodReference);

        return result;
    }

    public static class Builder {
        private String shortName;
        private String longName;
        private Method methodReference;

        public Builder shortName(String shortName) {
            if (null != shortName && shortName.length() > 0) {
                this.shortName = shortName;
            }

            return this;
        }

        public Builder longName(String longName) {
            if (null != longName && longName.length() > 0) {
                this.longName = longName;
            }

            return this;
        }

        Builder methodReference(Method methodReference) {
            if (null != methodReference) {
                this.methodReference = methodReference;
            }

            return this;
        }

        public SchemeArgument build() {
            return new SchemeArgument(
                    this.shortName,
                    this.longName,
                    this.methodReference
            );
        }
    }
}
