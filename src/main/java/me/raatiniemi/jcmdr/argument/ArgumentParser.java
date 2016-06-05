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

import java.util.*;

/**
 * Handle parsing of arguments against a predefined argument scheme.
 */
public class ArgumentParser {
    private static final String PREFIX_GNU_OPTION = "--";
    private static final String PREFIX_POSIX_OPTION = "-";

    private String arguments;
    private List<SchemeArgument> schemeArguments;
    private Set<ParsedArgument> parsedArguments = new LinkedHashSet<>();

    /**
     * Construct the argument parser.
     *
     * @param arguments       Arguments to parse against the argument scheme.
     * @param schemeArguments Argument scheme used for parsing.
     */
    public ArgumentParser(String arguments, List<SchemeArgument> schemeArguments) {
        this.arguments = arguments;
        this.schemeArguments = schemeArguments;
    }

    private static boolean isGnuOption(String argumentSegment) {
        return argumentSegment.startsWith(PREFIX_GNU_OPTION);
    }

    /**
     * Parse the arguments against the argument scheme.
     *
     * @return Arguments parsed against the argument scheme.
     */
    public Collection<ParsedArgument> parse() {
        if (isMissingArgumentScheme()) {
            return Collections.emptySet();
        }

        if (isMissingArguments()) {
            return Collections.emptySet();
        }

        return parseArgumentSegments();
    }

    private boolean isMissingArgumentScheme() {
        return null == this.schemeArguments || this.schemeArguments.isEmpty();
    }

    private boolean isMissingArguments() {
        return null == this.arguments || 0 == this.arguments.length();
    }

    private Collection<ParsedArgument> parseArgumentSegments() {
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

        if (argumentHaveValue(argument)) {
            String[] argumentWithValue = argument.split("=", 2);
            collectParsedArgument(
                    argumentWithValue[0],
                    argumentWithValue[1]
            );
            return;
        }

        collectParsedArgument(argument);
    }

    private boolean argumentHaveValue(String argument) {
        return argument.contains("=");
    }

    private void parsePosixArgumentSegment(String argumentSegment) {
        String argument = argumentSegment.replace(PREFIX_POSIX_OPTION, "");

        for (char character : argument.toCharArray()) {
            collectParsedArgument(String.valueOf(character));
        }
    }

    private void collectParsedArgument(String argument) {
        for (SchemeArgument schemeArgument : this.schemeArguments) {
            if (schemeArgument.validate(argument)) {
                ParsedArgument parsedArgument = new ParsedArgument.Builder()
                        .schemeArgument(schemeArgument)
                        .build();

                this.parsedArguments.add(parsedArgument);
                break;
            }
        }
    }

    private void collectParsedArgument(String argument, String argumentValue) {
        for (SchemeArgument schemeArgument : this.schemeArguments) {
            if (schemeArgument.validate(argument, String.class)) {
                ParsedArgument parsedArgument = new ParsedArgument.Builder()
                        .schemeArgument(schemeArgument)
                        .argumentValue(argumentValue)
                        .build();

                this.parsedArguments.add(parsedArgument);
                break;
            }
        }
    }
}
