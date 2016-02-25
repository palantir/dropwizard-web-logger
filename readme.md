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

	{"idNumber":"42","title":"batman","userName":"bruce wayne”}

*and the following configuration:*

	webLogger:
	  events:
	    - type: userLogin
	      fields: [idNumber, userName, title]

####A Line is Logged
Logger will log the following line to a file on a specific backend

	{"idNumber":"42","EventType":"userLogin","title":"batman","userName":"bruce wayne","timestamp":"2016-02-18 12:23:54 UTC"}

###Fixed Fields
Fixed fields will be added to all logged lines.

| Type   | Description                                                                                                                                  |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------|
| Timestamp | The timestamp of the log will be logged as *yyyy-MM-dd HH:mm:ss z* in UTC |
| EventType | The event type defined in the web-logger-bundle configuration |

Usage
-----

1.  Add the ``com.palantir.dropwizard:dropwizard-web-logger:<VERSION>`` dependency to your project's build.gradle file.
    The most recent version number can be found by looking at the [Releases Page](http://ivy.yojoe.local/artifactory/repo/com.palantir.dropwizard/dropwizard-web-logger/).
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
		      events:
		        - type: <eventTypeName>
		          fields: [<fields>]
		          enabled: <true|false> # optional - defaults to true
		        - type: <eventTypeName>
		          fields: [<fields]
		          enabled: <true|false> # optional - defaults to true
Example
        
			webLogger:
			  enabled: true
			  events:
			    - type: userLogin
			      fields: [idNumber, userName, title]
			    - type: userAuthLog
			      fields: [idNumber, userName, someOtherField]

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



Authentication and Security
---------------------------

While it is possible to use this bundle without requiring user authentication
or handling possible XSRF (cross-site request forgery) issues, the data
collected by the event logger would likely not be useful.

*It is strongly recommended that this bundle be used together with authentication and XSRF prevention.*
