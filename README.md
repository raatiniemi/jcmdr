# jcmdr
[![pipeline status](https://gitlab.com/raatiniemi/jcmdr/badges/master/pipeline.svg)](https://gitlab.com/raatiniemi/jcmdr/commits/master)
[![quality gate](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi%3Ajcmdr&metric=alert_status)](https://sonarcloud.io/dashboard?id=me.raatiniemi%3Ajcmdr)
[![code test coverage](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi%3Ajcmdr&metric=coverage)](https://sonarcloud.io/dashboard?id=me.raatiniemi%3Ajcmdr)
[![technical dept](https://sonarcloud.io/api/project_badges/measure?project=me.raatiniemi%3Ajcmdr&metric=sqale_index)](https://sonarcloud.io/dashboard?id=me.raatiniemi%3Ajcmdr)

jcmdr is a Java library for parsing command line arguments using annotations.

The idea is that you'll annotate methods on a class and send the class for
processing along with the arguments from the `main`-method.

```java
public class Commander {
    public static void main(String... arguments) {
        // Instantiate the target for parsing and send the instance with the
        // arguments to the CommandLine.process-method. This'll allow you to
        // setup the dependencies when instantiating Commander.
        Commander commander = new Commander();
        CommandLine.process(commander, arguments);

        if (commander.isDebugEnabled()) {
            System.out.println("Debug is enabled");
        }
    }

    private boolean isDebugEnabled = false;

    @Argument(shortName = "d", longName = "debug")
    public void enableDebug() {
        isDebugEnabled = true;
    }

    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }
}
```

To enable debug with the example code above, you can use either the `-d` format
or `--debug` when executing your `jar`-file.

## Execution flow

When the `CommandLine.process` receives an instance the following things happens.

1. Annotations are collected from the class, i.e. the argument scheme is parsed.
1. Incoming arguments are parsed and validated against the argument scheme, parsed arguments included in the argument scheme are collected.
1. Parsed arguments are invoked.

## License

```
Copyright (C) 2018 Tobias Raatiniemi

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
