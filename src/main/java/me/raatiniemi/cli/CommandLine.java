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

package me.raatiniemi.cli;

import me.raatiniemi.cli.argument.Argument;
import me.raatiniemi.cli.argument.ArgumentParser;
import me.raatiniemi.cli.scheme.SchemeArgument;
import me.raatiniemi.cli.scheme.SchemeParser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

class CommandLine<T> {
    private T target;
    private String[] args;
    private SchemeParser schemeParser;

    private CommandLine(T target, String[] args) {
        this.target = target;
        this.args = args;
        this.schemeParser = new SchemeParser(target.getClass());
    }

    static <T> void process(T target, String... args)
            throws InvocationTargetException, IllegalAccessException {
        CommandLine<T> commandLine = new CommandLine<>(target, args);
        commandLine.processArguments();
    }

    private void processArguments()
            throws InvocationTargetException, IllegalAccessException {
        for (Argument argument : getArguments()) {
            argument.call(this.target);
        }
    }

    private List<Argument> getArguments() {
        return getArgumentParser().parse();
    }

    private ArgumentParser getArgumentParser() {
        return new ArgumentParser(
                buildArguments(),
                parseSchemeArgument()
        );
    }

    private String buildArguments() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String arg : this.args) {
            stringBuilder.append(arg);
        }

        return stringBuilder.toString();
    }

    private List<SchemeArgument> parseSchemeArgument() {
        return this.schemeParser.parse();
    }
}
