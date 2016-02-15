WebLoggerBundle
===============
[![Circle CI](https://circleci.com/gh/palantir/dropwizard-web-logger.svg?style=svg&circle-token=ef99a2065c608bd3fd6237eff9034488275d6582)](https://circleci.com/gh/palantir/dropwizard-web-logger)


``WebLoggerBundle`` is a Dropwizard bundle used to help log web activity to log files on a server's backend.


Field Types
-----------
###Optional Fields
A user has the option of including fields with the types listed below.

| Type   | Description                                                                                                                                  |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------|
| STRING | The bundle expects a string value                                                                                                            |
| INT    | The bundle expects an integer value                                                                                                          |
| DATE   | The bundle expects a date passed as a *long* in epoch time. The date will be logged as by SimpleDateFormat as *yyyy-MM-dd HH:mm:ss z* in UTC |

###Fixed Fields
Fixed fields will be added to all logged lines.

| Type   | Description                                                                                                                                  |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------|
| TIMESTAMP | The timestamp of the log will be logged as *yyyy-MM-dd HH:mm:ss z* in UTC |


**Example**

*server.yml*

    webLogger:
      enabled: true
      fields:
        - field: id
          type: string
        - field: name
          type: string
        - field: description
          type: string
        - field: mynumber
          type: int
        - field: mydate
          type: date

    logging:
      appenders:
        - type: web-logger
          currentLogFilename: ./var/log/fe-logger-usage.json.log
          archivedLogFilenamePattern: ./var/log/fe-logger-usage-%d.json.log
          archivedFileCount: 5

::

    {
       "id":"12aoi312",
       "name":"bruce wayne",
       "description":"a fine superhero",
       "mynumber":42,
       "mydate":"1452788097"
    }

Usage
-----

1.  Add the ``com.palantir.dropwizard:dropwizard-web-logger:<VERSION>`` dependency to your project's build.gradle file.
    The most recent version number can be found by looking at the `Ivy repository listing <http://ivy.yojoe.local/artifactory/repo/com.palantir.dropwizard/dropwizard-web-logger/>`_.
    The dependencies section should look something like this:

    .. code-block:: none

       dependencies {
         // ... unrelated dependencies omitted ...

         compile "com.palantir.dropwizard:dropwizard-web-logger:<VERSION>"
       }

2.  Modify your server's configuration file

    1. Add ``webLogger`` to your yml file and enable it.
        ``server.yml``
		``` yml
            webLogger:
              enabled: <true|false>
              fields:
                - field: <name of field>
                  type: <string|int|date>
                - <Additional field / type pairs>
		```
        Example
        
            webLogger:
              enabled: true
              fields:
                - field: id
                  type: string
                - field: myFavoriteNumber
                  type: int
                - field: myBirthday
                  type: date

        See above for possible field types.

    2. Add an appender of type ``web-logger`` to your Dropwizard configuration YAML in the logging section:
        ``server.yml``

        .. code-block:: yaml

          - type: web-logger
            currentLogFileName: ./var/log/<APPNAME>-usage.json.log
            archivedLogFilenamePattern: ./var/log/<APPNAME>-usage-%d.json.log
            archivedFileCount: <NUMBER_OF_LOGFILES_TO_ARCHIVE>

        Example

        .. code-block:: yaml

            logging:
              appenders:
                - type: file
                  currentLogFilename: var/log/server.log
                  archivedLogFilenamePattern: var/log/server-%d.log
                  archivedFileCount: 5
                  timeZone: UTC
                  threshold: INFO
                - type: console
                  threshold: INFO
                - type: web-logger
                  currentLogFilename: ./var/log/fe-logger-usage.json.log
                  archivedLogFilenamePattern: ./var/log/fe-logger-usage-%d.json.log
                  archivedFileCount: 5

3. Have your configuration implement ``WebLoggerConfigurationProvider``:
    .. code-block:: java

        public final class ExampleApplicationConfiguration extends Configuration
                implements WebLoggerConfigurationProvider {

            private final WebLoggerConfiguration webLogger;

            @JsonCreator
            public ExampleApplicationConfiguration(
                @JsonProperty("webLogger") WebLoggerConfiguration webLogger) {

                this.webLogger = webLogger;
            }

            @Override
            public WebLoggerConfiguration getWebLogger() {
                return this.webLogger;
            }
        }

4. Add the bundle to your Dropwizard application.
    .. code-block:: java

        @Override
        public void initialize(Bootstrap<ExampleApplicationConfiguration> bootstrap) {
            bootstrap.addBundle(new WebLoggerBundle());
        }



Authentication and Security
---------------------------

While it is possible to use this bundle without requiring user authentication
or handling possible XSRF (cross-site request forgery) issues, the data
collected by the event logger would likely not be useful.

*It is strongly recommended that this bundle be used together with authentication and XSRF prevention.*


Working Example
---------------

See `here <https://stash.yojoe.local/users/mzoubeiri/repos/dropwizard-web-logger-example/browse>`_ for a working example.

