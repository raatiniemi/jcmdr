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

import java.util.Collection;
import java.util.List;

/**
 * Handle processing of arguments.
 *
 * @param <T> Type reference used for parsing the argument scheme.
 */
public class CommandLine<T> {
    private T target;
    private String[] args;
    private SchemeParser schemeParser;

    private CommandLine(T target, String[] args) {
        this.target = target;
        this.args = args;
        this.schemeParser = SchemeParserFactory.createFor(target.getClass());
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
        for (ParsedArgument parsedArgument : getParsedArguments()) {
            parsedArgument.call(this.target);
        }
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
        StringBuilder arguments = new StringBuilder();

        for (String arg : this.args) {
            arguments.append(" ")
                    .append(arg);
        }

        return arguments.toString();
    }

    private List<SchemeArgument> parseSchemeArgument() {
        return this.schemeParser.parse();
    }
}
