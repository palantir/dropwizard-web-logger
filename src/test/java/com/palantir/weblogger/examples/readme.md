Usage:

1. Run com.palantir.weblogger.examples.Example. Program args: ``server ./src/test/resources/server.yml``

2. In terminal:
        
    curl -H "Content-Type: application/json" -X POST -d '{"username":"xyz","password":"xyz"}' http://localhost:8000/example/web-logger/events/jump
    
3. See ``dropwizard-web-logger/src/test/resources/log/fe-logger-usage.json.log`` for logged events