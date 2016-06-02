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

public class SchemeArgument {
    private final String shortName;
    private final String longName;
    private final Method methodReference;

    private Class<?>[] methodReferenceParameterTypes;

    private SchemeArgument(
            String shortName,
            String longName,
            Method methodReference
    ) {
        if (null == shortName && null == longName) {
            throw new InvalidSchemeArgumentException(
                    "Short and/or long name must be supplied"
            );
        }

        this.shortName = shortName;
        this.longName = longName;
        this.methodReference = methodReference;
    }

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

        if (null == this.methodReference) {
            return !haveArgumentValueTypes;
        }

        return Arrays.equals(
                getMethodReferenceParameterTypes(),
                argumentValueTypes
        );
    }

    public <T> void call(T target, String argumentValue) throws InvokeArgumentException {
        try {
            if (null == argumentValue) {
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
        return null == this.methodReferenceParameterTypes;
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

        if (!(o instanceof SchemeArgument)) {
            return false;
        }

        SchemeArgument argument = (SchemeArgument) o;
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
        return null == this.methodReference;
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
            if (null == shortName || 0 == shortName.length()) {
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
            if (null == longName || 0 == longName.length()) {
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
            if (null != methodReference) {
                this.methodReference = methodReference;
            }

            return this;
        }

        SchemeArgument build() {
            return new SchemeArgument(
                    this.shortName,
                    this.longName,
                    this.methodReference
            );
        }
    }
}
