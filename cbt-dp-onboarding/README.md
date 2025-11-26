# Bigtable DirectPath onboarding for engineers

## Overview
This project contains some sample code to help with onboarding CBT engineers onto DirectPath. This
repo contains some client code snippets that help with the e2e understanding of DirectPath.

## Samples

### Setup
Compile the source code using:

```shell
mvn compile
```

To enable verbose logging in any of the examples, append this to the commandline:
```shell
-Djava.util.logging.config.file=src/main/resources/logging.properties
```

### CloudPath

Just a very simple example of connecting to bigtable using CloudPath. Primarily meant as a baseline
for comparison. To run:

```shell
mvn exec:java -Dexec.mainClass=CloudPath
```

### RawDirectPath

Minimal example demonstrating how to connect via directpath using only the data plane (ie no 
TrafficDirector discovery). To run, first get the AFE ip address and port from the borg machine's
USPS page. See here for instructions: go/cbt-setup-directpath-endpoint#step-1-ensure-that-the-target-job-is-properly-setup.
Then run the following command

```shell
mvn exec:java -Dexec.mainClass=RawDirectPath \
  -Dbigtable.host="<ip address>" -Dbigtable.port=<port>
```
