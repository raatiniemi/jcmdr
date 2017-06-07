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

import me.raatiniemi.jcmdr.exception.InvokeArgumentException;
import me.raatiniemi.jcmdr.scheme.exception.InvalidLongNameException;
import me.raatiniemi.jcmdr.scheme.exception.InvalidSchemeArgumentException;
import me.raatiniemi.jcmdr.scheme.exception.InvalidShortNameException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static me.raatiniemi.jcmdr.helper.Strings.isNullOrEmpty;

class MethodSchemeArgument implements SchemeArgument {
    private final String shortName;
    private final String longName;
    private final Method methodReference;

    private Class<?>[] methodReferenceParameterTypes;

    private MethodSchemeArgument(
            String shortName,
            String longName,
            Method methodReference
    ) {
        if (isNull(shortName) && isNull(longName)) {
            throw new InvalidSchemeArgumentException(
                    "Short and/or long name must be supplied"
            );
        }

        this.shortName = shortName;
        this.longName = longName;
        this.methodReference = methodReference;
    }

    @Override
    public boolean validate(String argument, Class<?>... argumentValueTypes) {
        return validateArgument(argument)
                && validateArgumentValueTypes(argumentValueTypes);
    }

    private boolean validateArgument(String argument) {
        return argument.equals(this.shortName)
                || argument.equalsIgnoreCase(this.longName);
    }

    private boolean validateArgumentValueTypes(Class<?>[] argumentValueTypes) {
        boolean haveArgumentValueTypes = argumentValueTypes.length > 0;

        if (isNull(methodReference)) {
            return !haveArgumentValueTypes;
        }

        return Arrays.equals(
                getMethodReferenceParameterTypes(),
                argumentValueTypes
        );
    }

    @Override
    public <T> void call(T target, String argumentValue) {
        try {
            if (isNull(argumentValue)) {
                this.methodReference.invoke(target);
                return;
            }

            this.methodReference.invoke(target, argumentValue);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new InvokeArgumentException(e);
        }
    }

    private Class<?>[] getMethodReferenceParameterTypes() {
        if (shouldCacheMethodReferenceParameterTypes()) {
            cacheMethodReferenceParameterTypes();
        }

        return this.methodReferenceParameterTypes;
    }

    private boolean shouldCacheMethodReferenceParameterTypes() {
        return isNull(methodReferenceParameterTypes);
    }

    private void cacheMethodReferenceParameterTypes() {
        this.methodReferenceParameterTypes = this.methodReference.getParameterTypes();
    }

    @Override
    public String toString() {
        return "{shortName='" + shortName + "', longName='" + longName + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MethodSchemeArgument)) {
            return false;
        }

        MethodSchemeArgument argument = (MethodSchemeArgument) o;
        return Objects.equals(this.shortName, argument.shortName)
                && Objects.equals(this.longName, argument.longName)
                && Objects.equals(this.methodReference, argument.methodReference);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Objects.hashCode(this.shortName);
        result = 31 * result + Objects.hashCode(this.longName);

        return calculateHashCodeForMethodReference(result);
    }

    private int calculateHashCodeForMethodReference(int calculatedHashCode) {
        if (isMethodReferenceMissing()) {
            return calculatedHashCode;
        }

        int result = 31 * calculatedHashCode + Objects.hashCode(this.methodReference);
        return calculateHashCodeForMethodReferenceArguments(result);
    }

    private boolean isMethodReferenceMissing() {
        return isNull(methodReference);
    }

    private int calculateHashCodeForMethodReferenceArguments(int calculatedHashCode) {
        if (isMethodReferenceMissingParameters()) {
            return calculatedHashCode;
        }

        return 31 * calculatedHashCode + Objects.hashCode(getMethodReferenceParameterTypes());
    }

    private boolean isMethodReferenceMissingParameters() {
        return 0 == getMethodReferenceParameterTypes().length;
    }

    static class Builder {
        private String shortName;
        private String longName;
        private Method methodReference;

        Builder shortName(String shortName) {
            if (isNullOrEmpty(shortName)) {
                return this;
            }

            if (1 != shortName.length()) {
                throw new InvalidShortNameException(
                        "Short name must be one character"
                );
            }

            this.shortName = shortName;
            return this;
        }

        Builder longName(String longName) {
            if (isNullOrEmpty(longName)) {
                return this;
            }

            if (1 == longName.length()) {
                throw new InvalidLongNameException(
                        "Long name can not be one character"
                );
            }

            this.longName = longName.toLowerCase();
            return this;
        }

        Builder methodReference(Method methodReference) {
            if (nonNull(methodReference)) {
                this.methodReference = methodReference;
            }

            return this;
        }

        MethodSchemeArgument build() {
            return new MethodSchemeArgument(
                    this.shortName,
                    this.longName,
                    this.methodReference
            );
        }
    }
}
