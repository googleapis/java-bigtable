# CBT Java Test Proxy

The CBT test proxy is intended for running confromance tests for Cloug Bigtable Java Client.

## Set up

If you have not already done so, [install golang](https://go.dev/doc/install), then clone the go test library:

```
git clone https://github.com/googleapis/cloud-bigtable-clients-test.git
```

## Start test proxy

Build the proxy with the latest/head version of the client

```
cd java-bigtable
mvn clean install -DskipTests
cd test-proxy
mvn clean install
```

To build the proxy with an older version of the client, please download the
source code [here](https://github.com/googleapis/java-bigtable/releases),
and repeat the build steps mentioned above.

Start the proxy on default port 9999

```
java -jar target/google-cloud-bigtable-test-proxy-<proxy_version>.jar
```

Start the proxy on a different port

```
java -Dport=1 -jar target/google-cloud-bigtable-test-proxy-<proxy_version>.jar
```

## Run the test cases

```
cd cloud-bigtable-clients-test/tests
go test -v -proxy_addr=:9999
```
