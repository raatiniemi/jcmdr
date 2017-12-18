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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static me.raatiniemi.jcmdr.helper.Strings.isNullOrEmpty;

/**
 * Handle parsing of arguments against a predefined argument scheme.
 */
public final class ArgumentParser {
    private static final String PREFIX_JAVA_OPTION = "-D";
    private static final String PREFIX_LONG_NAME = "--";
    private static final String PREFIX_SHORT_NAME = "-";
    private static final String VALUE_SEPARATOR = "=";

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

    private static boolean isJavaOption(String argumentSegment) {
        return argumentSegment.startsWith(PREFIX_JAVA_OPTION);
    }

    private static boolean isLongName(String argumentSegment) {
        return argumentSegment.startsWith(PREFIX_LONG_NAME);
    }

    private static boolean argumentHaveValue(String argument) {
        return argument.contains(VALUE_SEPARATOR);
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
        return isNull(schemeArguments) || schemeArguments.isEmpty();
    }

    private boolean isMissingArguments() {
        return isNullOrEmpty(arguments);
    }

    private Collection<ParsedArgument> parseArgumentSegments() {
        getArgumentSegments().forEach(this::parseArgumentSegment);

        return parsedArguments;
    }

    private void parseArgumentSegment(String argumentSegment) {
        if (isJavaOption(argumentSegment)) {
            parseJavaOption(argumentSegment).ifPresent(parsedArguments::add);
            return;
        }

        if (isLongName(argumentSegment)) {
            parseLongName(argumentSegment).ifPresent(parsedArguments::add);
            return;
        }

        parsedArguments.addAll(parseShortName(argumentSegment));
    }

    private Stream<String> getArgumentSegments() {
        return Arrays.stream(arguments.split(" "));
    }

    private Optional<ParsedArgument> parseJavaOption(String argumentSegment) {
        String argument = argumentSegment.replaceFirst(PREFIX_JAVA_OPTION, "");

        if (argumentHaveValue(argument)) {
            String[] argumentWithValue = argument.split(VALUE_SEPARATOR, 2);
            return collectParsedArgument(
                    argumentWithValue[0],
                    argumentWithValue[1]
            );
        }

        return collectParsedArgument(argument);
    }

    private Optional<ParsedArgument> parseLongName(String argumentSegment) {
        String argument = argumentSegment.replace(PREFIX_LONG_NAME, "");

        if (argumentHaveValue(argument)) {
            String[] argumentWithValue = argument.split(VALUE_SEPARATOR, 2);
            return collectParsedArgument(
                    argumentWithValue[0],
                    argumentWithValue[1]
            );
        }

        return collectParsedArgument(argument);
    }

    private List<ParsedArgument> parseShortName(String argumentSegment) {
        String argument = argumentSegment.replace(PREFIX_SHORT_NAME, "");

        return argument.chars()
                .mapToObj(i -> (char) i)
                .map(String::valueOf)
                .map(this::collectParsedArgument)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<ParsedArgument> collectParsedArgument(String argument) {
        return schemeArguments.parallelStream()
                .filter(schemeArgument -> schemeArgument.validate(argument))
                .map(schemeArgument -> new ParsedArgumentImpl.Builder()
                        .schemeArgument(schemeArgument)
                        .build())
                .findFirst();
    }

    private Optional<ParsedArgument> collectParsedArgument(String argument, String argumentValue) {
        return schemeArguments.parallelStream()
                .filter(schemeArgument -> schemeArgument.validate(argument, String.class))
                .map(schemeArgument -> new ParsedArgumentImpl.Builder()
                        .schemeArgument(schemeArgument)
                        .argumentValue(argumentValue)
                        .build())
                .findFirst();
    }
}
