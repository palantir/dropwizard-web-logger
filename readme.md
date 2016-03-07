Dropwizard Web Logger
=====================
[![Circle CI](https://circleci.com/gh/palantir/dropwizard-web-logger.svg?style=svg&circle-token=ef99a2065c608bd3fd6237eff9034488275d6582)](https://circleci.com/gh/palantir/dropwizard-web-logger)
[ ![Download](https://api.bintray.com/packages/palantir/releases/dropwizard-web-logger/images/download.svg) ](https://bintray.com/palantir/releases/dropwizard-web-logger/_latestVersion)


``WebLoggerBundle`` is a Dropwizard bundle used to help log web activity to log files on a server's backend.


###Example

####Make Call
*Send a POST request to:*

	http://localhost:8000/<web-app-name>/api/web-logger

*with the body content:*

	{"eventName":"jump","height":"42feet","name":"douglas”}

*and the following configuration:*

	webLogger:
	  eventNames: [jump, highfive]

####A Line is Logged
Logger will log the following line to a file on a specific backend

	{"eventName":"jump","name":"douglas","timestamp":"2016-02-18 12:23:54 UTC"}

###Fixed Fields
Fixed fields will be added to all logged lines.

| Type   | Description                                                                                                                                  |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------|
| Timestamp | The timestamp of the log will be logged as *yyyy-MM-dd HH:mm:ss z* in UTC |

Usage
-----

1.  Add the ``com.palantir.weblogger:dropwizard-web-logger:<VERSION>`` dependency to your project's build.gradle file.
    The most recent version number can be found by looking at the [Releases Page](https://github.com/palantir/dropwizard-web-logger/releases).
    The dependencies section should look something like this:

		dependencies {
			// ... unrelated dependencies omitted ...
			compile "com.palantir.dropwizard:dropwizard-web-logger:<VERSION>"
		}

2.  Modify your server's **configuration** file

    a. Add ``webLogger`` to your yml file and enable it.
        ``server.yml``

		    webLogger:
		      enabled: <true|false> # optional - defaults to true
		      eventNames: [<EventNames>]
	   Example
        
			webLogger:
			  enabled: true
			  eventNames: [jump, highfive, run]

    b. Add an appender of type ``web-logger`` to your Dropwizard configuration YAML in the logging section:
        ``server.yml``

	          - type: web-logger
	            currentLogFileName: ./var/log/<APPNAME>-usage.json.log
	            archivedLogFilenamePattern: ./var/log/<APPNAME>-usage-%d.json.log
	            archivedFileCount: <NUMBER_OF_LOGFILES_TO_ARCHIVE>

	   Example

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

        @Override
        public void initialize(Bootstrap<ExampleApplicationConfiguration> bootstrap) {
            bootstrap.addBundle(new WebLoggerBundle());
        }

Setting up the project with an IDE
----------------------------------
with Eclipse, import the project and run:

        ./gradlew eclipse

with IntelliJ, import the project and run:

        ./gradlew idea

Authentication and Security
---------------------------

While it is possible to use this bundle without requiring user authentication
or handling possible XSRF (cross-site request forgery) issues, the data
collected by the event logger would likely not be useful.

*It is strongly recommended that this bundle be used together with authentication and XSRF prevention.*
