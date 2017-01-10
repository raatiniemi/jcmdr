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

import java.lang.reflect.Method;

@SuppressWarnings("unused")
class SchemeArgumentImplTestReference {
    static Method getMethodReference(
            String methodName,
            Class... arguments
    ) throws NoSuchMethodException {
        return SchemeArgumentImplTestReference.class.getMethod(methodName, arguments);
    }

    public void firstMethodWithoutArguments() {
    }

    public void secondMethodWithoutArguments() {
    }

    public void methodWithArgument(String argument) {
    }

    public void methodWithArgument(Long argument) {
    }

    public void methodWithArguments(
            String firstArgument,
            Long secondArgument
    ) {
    }

    public void methodWithArguments(
            Long firstArgument,
            String secondArgument
    ) {
    }
}
