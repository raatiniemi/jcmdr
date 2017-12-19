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
import java.util.function.Function;
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

    private final Function<String, String> removeJavaPrefix = s -> s.replaceFirst(PREFIX_JAVA_OPTION, "");
    private final Function<String, String> removeLongNamePrefix = s -> s.replaceFirst(PREFIX_LONG_NAME, "");
    private final Function<String, String> removeShortNamePrefix = s -> s.replaceFirst(PREFIX_SHORT_NAME, "");

    private String arguments;
    private List<SchemeArgument> schemeArguments;

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
        return getArgumentSegments()
                .flatMap(this::processArgumentSegment)
                .map(this::parseArgumentSegment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Stream<String> getArgumentSegments() {
        return Arrays.stream(arguments.split(" "));
    }

    private Stream<String> processArgumentSegment(String argumentSegment) {
        Stream<String> stream = Stream.of(argumentSegment);

        if (isJavaOption(argumentSegment)) {
            return stream.map(removeJavaPrefix);
        }

        if (isLongName(argumentSegment)) {
            return stream.map(removeLongNamePrefix);
        }

        return stream.map(removeShortNamePrefix)
                .flatMap(s -> s.chars()
                        .mapToObj(i -> (char) i)
                        .map(String::valueOf));
    }

    private Optional<ParsedArgument> parseArgumentSegment(String argumentSegment) {
        if (argumentHaveValue(argumentSegment)) {
            String[] argumentWithValue = argumentSegment.split(VALUE_SEPARATOR, 2);
            return collectParsedArgument(
                    argumentWithValue[0],
                    argumentWithValue[1]
            );
        }

        return collectParsedArgument(argumentSegment);
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
