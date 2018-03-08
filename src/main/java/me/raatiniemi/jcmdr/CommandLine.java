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

package me.raatiniemi.jcmdr;

import me.raatiniemi.jcmdr.argument.ArgumentParser;
import me.raatiniemi.jcmdr.argument.ParsedArgument;
import me.raatiniemi.jcmdr.scheme.SchemeArgument;
import me.raatiniemi.jcmdr.scheme.SchemeParser;
import me.raatiniemi.jcmdr.scheme.SchemeParserFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle processing of arguments.
 *
 * @param <T> Type reference used for parsing the argument scheme.
 */
public final class CommandLine<T> {
    private final T target;
    private final String[] args;
    private final SchemeParser schemeParser;

    private CommandLine(T target, String[] args) {
        this.target = target;
        this.args = args;

        schemeParser = SchemeParserFactory.createFor(target.getClass());
    }

    /**
     * Process the arguments.
     *
     * @param target Instance used for parsing the argument scheme.
     * @param args   Arguments to process.
     * @param <T>    Type reference used for parsing the argument scheme.
     */
    public static <T> void process(T target, String... args) {
        CommandLine<T> commandLine = new CommandLine<>(target, args);
        commandLine.processArguments();
    }

    private void processArguments() {
        getParsedArguments().forEach(parsedArgument -> parsedArgument.call(target));
    }

    private Collection<ParsedArgument> getParsedArguments() {
        return getArgumentParser().parse();
    }

    private ArgumentParser getArgumentParser() {
        return new ArgumentParser(
                buildArguments(),
                parseSchemeArgument()
        );
    }

    private String buildArguments() {
        return Arrays.stream(args)
                .collect(Collectors.joining(" "));
    }

    private List<SchemeArgument> parseSchemeArgument() {
        return schemeParser.parse();
    }
}
