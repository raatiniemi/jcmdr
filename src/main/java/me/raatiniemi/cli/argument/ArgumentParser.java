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

public class ArgumentParser {
    private static final String PREFIX_GNU_OPTION = "--";
    private static final String PREFIX_POSIX_OPTION = "-";

    private String arguments;
    private List<SchemeArgument> schemeArguments;
    private List<ParsedArgument> parsedArguments = new ArrayList<>();

    public ArgumentParser(String arguments, List<SchemeArgument> schemeArguments) {
        this.arguments = arguments;
        this.schemeArguments = schemeArguments;
    }

    private static boolean isGnuOption(String argumentSegment) {
        return argumentSegment.startsWith(PREFIX_GNU_OPTION);
    }

    public List<ParsedArgument> parse() {
        if (isMissingArgumentScheme()) {
            return Collections.emptyList();
        }

        if (isMissingArguments()) {
            return Collections.emptyList();
        }

        return parseArgumentSegments();
    }

    private boolean isMissingArgumentScheme() {
        return null == this.schemeArguments || this.schemeArguments.isEmpty();
    }

    private boolean isMissingArguments() {
        return null == this.arguments || 0 == this.arguments.length();
    }

    private List<ParsedArgument> parseArgumentSegments() {
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

    private void parseGnuOption(String argumentSegment) {
        String argument = argumentSegment.replace(PREFIX_GNU_OPTION, "");

        for (SchemeArgument schemeArgument : this.schemeArguments) {
            if (schemeArgument.validate(argument)) {
                this.parsedArguments.add(new ParsedArgument(schemeArgument));
                break;
            }
        }
    }

    private void parsePosixArgumentSegment(String argumentSegment) {
        String argument = argumentSegment.replace(PREFIX_POSIX_OPTION, "");
        char[] options = argument.toCharArray();

        for (char character : options) {
            String option = String.valueOf(character);
            for (SchemeArgument schemeArgument : this.schemeArguments) {
                if (schemeArgument.validate(option)) {
                    this.parsedArguments.add(new ParsedArgument(schemeArgument));
                    break;
                }
            }
        }
    }
}
