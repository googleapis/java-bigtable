# Code organization
The maven project is split into two modules:
- [sink](sink) - the sink and its unit tests
- [integration-tests](integration-tests) - the integration tests

This split enables two desirable properties for the integration tests:
- the versions of dependencies used by the integration tests and the sink may be different (since Kafka Connect isolates connectors' class loaders automatically),
- the sink is provided to the integration tests with a directory of jars just like in a real Kafka Connect deployment.

# Tests
For details on running the tests, please see [doc/tests.md](doc/tests.md).

# Performance test setup
The performance test setup is described in detail in [doc/performance/README.md](doc/performance/README.md).
