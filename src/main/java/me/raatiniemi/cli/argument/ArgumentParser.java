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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ArgumentParser {
    private String arguments;
    private SchemeArgument[] schemeArguments;
    private List<Argument> parsedArguments = new ArrayList<>();

    ArgumentParser(String arguments, SchemeArgument[] schemeArguments) {
        this.arguments = arguments;
        this.schemeArguments = schemeArguments;
    }

    List<Argument> parse() {
        if (isMissingArgumentScheme()) {
            return Collections.emptyList();
        }

        if (isMissingArguments()) {
            return Collections.emptyList();
        }

        return parseArgumentSegments();
    }

    private boolean isMissingArgumentScheme() {
        return null == this.schemeArguments;
    }

    private boolean isMissingArguments() {
        return null == this.arguments || 0 == this.arguments.length();
    }

    private List<Argument> parseArgumentSegments() {
        for (String argumentSegment : getArgumentSegments()) {
            if (isGnuOption(argumentSegment)) {
                parseGnuOption(argumentSegment);
                continue;
            }

            parsePosixArgumentSegment(argumentSegment);
        }

        return this.parsedArguments;
    }

    private String[] getArgumentSegments() {
        return this.arguments.split(" ");
    }

    private boolean isGnuOption(String argumentSegment) {
        return argumentSegment.startsWith("--");
    }

    private void parseGnuOption(String argumentSegment) {
        argumentSegment = argumentSegment.replace("--", "");

        for (SchemeArgument schemeArgument : this.schemeArguments) {
            if (!schemeArgument.validate(argumentSegment)) {
                continue;
            }

            this.parsedArguments.add(new GnuOption(argumentSegment));
            break;
        }
    }

    private void parsePosixArgumentSegment(String argumentSegment) {
        argumentSegment = argumentSegment.replace("-", "");
        char[] options = argumentSegment.toCharArray();

        for (char character : options) {
            String option = String.valueOf(character);
            for (SchemeArgument schemeArgument : this.schemeArguments) {
                if (!schemeArgument.validate(option)) {
                    continue;
                }

                this.parsedArguments.add(new PosixOption(schemeArgument));
                break;
            }
        }
    }
}
