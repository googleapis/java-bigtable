# Google Cloud Bigtable Client for Java

Java idiomatic client for [Cloud Bigtable][product-docs].

[![Maven][maven-version-image]][maven-version-link]
![Stability][stability-image]

- [Product Documentation][product-docs]
- [Client Library Documentation][javadocs]


## Quickstart

If you are using Maven with [BOM][libraries-bom], add this to your pom.xml file:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.google.cloud</groupId>
      <artifactId>libraries-bom</artifactId>
      <version>26.37.0</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>

<dependencies>
  <dependency>
    <groupId>com.google.cloud</groupId>
    <artifactId>google-cloud-bigtable</artifactId>
  </dependency>

```

If you are using Maven without the BOM, add this to your dependencies:

<!-- {x-version-update-start:google-cloud-bigtable:released} -->

```xml
<dependency>
  <groupId>com.google.cloud</groupId>
  <artifactId>google-cloud-bigtable</artifactId>
  <version>2.39.5</version>
</dependency>

```

If you are using Gradle 5.x or later, add this to your dependencies:

```Groovy
implementation platform('com.google.cloud:libraries-bom:26.42.0')

implementation 'com.google.cloud:google-cloud-bigtable'
```
If you are using Gradle without BOM, add this to your dependencies:

```Groovy
implementation 'com.google.cloud:google-cloud-bigtable:2.39.5'
```

If you are using SBT, add this to your dependencies:

```Scala
libraryDependencies += "com.google.cloud" % "google-cloud-bigtable" % "2.39.5"
```
<!-- {x-version-update-end} -->

## Authentication

See the [Authentication][authentication] section in the base directory's README.

## Authorization

The client application making API calls must be granted [authorization scopes][auth-scopes] required for the desired Cloud Bigtable APIs, and the authenticated principal must have the [IAM role(s)][predefined-iam-roles] required to access GCP resources using the Cloud Bigtable API calls.

## Getting Started

### Prerequisites

You will need a [Google Cloud Platform Console][developer-console] project with the Cloud Bigtable [API enabled][enable-api].

[Follow these instructions][create-project] to get your project set up. You will also need to set up the local development environment by
[installing the Google Cloud Command Line Interface][cloud-cli] and running the following commands in command line:
`gcloud auth login` and `gcloud config set project [YOUR PROJECT ID]`.

### Installation and setup

You'll need to obtain the `google-cloud-bigtable` library.  See the [Quickstart](#quickstart) section
to add `google-cloud-bigtable` as a dependency in your code.

## About Cloud Bigtable


[Cloud Bigtable][product-docs] 

See the [Cloud Bigtable client library docs][javadocs] to learn how to
use this Cloud Bigtable Client Library.


## About Cloud Bigtable

[Cloud Bigtable][product-docs] is Google's NoSQL Big Data database service. It's
the same database that powers many core Google services, including Search, Analytics, Maps, and
Gmail.

Be sure to activate the Cloud Bigtable API and the Cloud Bigtable Admin API under APIs & Services in the GCP Console to use Cloud Bigtable from your project.

See the Bigtable client library documentation ([Admin API](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/admin/v2/package-summary.html) and [Data API](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/data/v2/package-summary.html)) to learn how to
interact with Cloud Bigtable using this Client Library.

## Concepts

Cloud Bigtable is composed of instances, clusters, nodes and tables.

### Instances
Instances are containers for clusters.

### Clusters
Clusters represent the actual Cloud Bigtable service. Each cluster belongs to a single Cloud Bigtable instance, and an instance can have up to 4 clusters. When your application
sends requests to a Cloud Bigtable instance, those requests are actually handled by one of the clusters in the instance.

### Nodes
Each cluster in a production instance has 3 or more nodes, which are compute resources that Cloud Bigtable uses to manage your data.

### Tables
Tables contain the actual data and are replicated across all of the clusters in an instance.


## Clients
The Cloud Bigtable API consists of:

### Data API
Allows callers to persist and query data in a table. It's exposed by [BigtableDataClient](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/data/v2/BigtableDataClient.html).

### Admin API
Allows callers to create and manage instances, clusters, tables, and access permissions. This API is exposed by: [BigtableInstanceAdminClient](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/admin/v2/BigtableInstanceAdminClient.html) for Instance and Cluster level resources.

See [BigtableTableAdminClient](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/admin/v2/BigtableTableAdminClient.html) for table management.

See [BigtableDataClient](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/data/v2/BigtableDataClient.html) for the data client.

See [BigtableInstanceAdminClient](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/admin/v2/BigtableInstanceAdminClient.html) for the instance admin client.

See [BigtableTableAdminClient](https://googleapis.dev/java/google-cloud-clients/latest/com/google/cloud/bigtable/admin/v2/BigtableTableAdminClient.html) for the table admin client.

#### Calling Cloud Bigtable

The Cloud Bigtable API is split into 3 parts: Data API, Instance Admin API and Table Admin API.

Here is a code snippet showing simple usage of the Data API. Add the following imports
at the top of your file:

```java
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;

```

Then, to make a query to Bigtable, use the following code:
```java
// Instantiates a client
String projectId = "my-project";
String instanceId = "my-instance";
String tableId = "my-table";

// Create the client.
// Please note that creating the client is a very expensive operation
// and should only be done once and shared in an application.
BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId);

try {
  // Query a table
  Query query = Query.create(tableId)
      .range("a", "z")
      .limit(26);

  for (Row row : dataClient.readRows(query)) {
    System.out.println(row.getKey());
  }
} finally {
  dataClient.close();
}
```

The Admin APIs are similar. Here is a code snippet showing how to create a table. Add the following
imports at the top of your file:

```java
import static com.google.cloud.bigtable.admin.v2.models.GCRules.GCRULES;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.Table;
```

Then, to create a table, use the following code:
```java
String projectId = "my-instance";
String instanceId = "my-database";

BigtableTableAdminClient tableAdminClient = BigtableTableAdminClient
  .create(projectId, instanceId);

try {
  tableAdminClient.createTable(
      CreateTableRequest.of("my-table")
        .addFamily("my-family")
  );
} finally {
  tableAdminClient.close();
}
```

TIP: If you are experiencing version conflicts with gRPC, see [Version Conflicts](#version-conflicts).

## Client side metrics

Cloud Bigtable client supports publishing client side metrics to
[Cloud Monitoring](https://cloud.google.com/monitoring/docs/monitoring-overview) under the
`bigtable.googleapis.com/client` namespace.

This feature is available once you upgrade to version 2.16.0 and above.
Follow the guide on https://cloud.google.com/bigtable/docs/client-side-metrics-setup to enable.

Since version 2.38.0, [client side metrics](https://cloud.google.com/bigtable/docs/client-side-metrics)
is enabled by default. This feature collects useful telemetry data in the client and is recommended to
use in conjunction with server-side metrics to get a complete, actionable view of your Bigtable
performance. There is no additional cost to publish and view client-side metrics
in Cloud Monitoring.

### Opt-out client side metrics

You can opt-out client side metrics with the following settings:

```java
BigtableDataSettings settings = BigtableDataSettings.newBuilder()
        .setProjectId("my-project")
        .setInstanceId("my-instance")
        .setMetricsProvider(NoopMetricsProvider.INSTANCE)
        .build();
```

### Use a custom OpenTelemetry instance

If your application already has OpenTelemetry integration, you can register client side metrics on
your OpenTelemetry instance. You can refer to
[CustomOpenTelemetryMetricsProvider](https://github.com/googleapis/java-bigtable/blob/main/google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/data/v2/stub/metrics/CustomOpenTelemetryMetricsProvider.java)
on how to set it up.

## Client request tracing: OpenCensus Tracing

Cloud Bigtable client supports [OpenCensus Tracing](https://opencensus.io/tracing/),
which gives insight into the client internals and aids in debugging production issues.
By default, the functionality is disabled. For example to enable tracing using
[Google Stackdriver](https://cloud.google.com/trace/docs/):

[//]: # (TODO: figure out how to keep opencensus version in sync with pom.xml)

If you are using Maven, add this to your pom.xml file
```xml
<dependency>
  <groupId>io.opencensus</groupId>
  <artifactId>opencensus-impl</artifactId>
  <version>0.31.1</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.opencensus</groupId>
  <artifactId>opencensus-exporter-trace-stackdriver</artifactId>
  <version>0.31.1</version>
  <exclusions>
    <exclusion>
      <groupId>io.grpc</groupId>
      <artifactId>*</artifactId>
    </exclusion>
    <exclusion>
      <groupId>com.google.auth</groupId>
      <artifactId>*</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```
If you are using Gradle, add this to your dependencies
```Groovy
compile 'io.opencensus:opencensus-impl:0.24.0'
compile 'io.opencensus:opencensus-exporter-trace-stackdriver:0.24.0'
```
If you are using SBT, add this to your dependencies
```Scala
libraryDependencies += "io.opencensus" % "opencensus-impl" % "0.24.0"
libraryDependencies += "io.opencensus" % "opencensus-exporter-trace-stackdriver" % "0.24.0"
```

At the start of your application configure the exporter:

```java
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;

StackdriverTraceExporter.createAndRegister(
  StackdriverTraceConfiguration.builder()
      .setProjectId("YOUR_PROJECT_ID")
      .build());
```

You can view the traces on the Google Cloud Platform Console
[Trace](https://console.cloud.google.com/traces) page.

By default traces are [sampled](https://opencensus.io/tracing/sampling) at a rate of about 1/10,000.
You can configure a higher rate by updating the active tracing params:

```java
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;

Tracing.getTraceConfig().updateActiveTraceParams(
    Tracing.getTraceConfig().getActiveTraceParams().toBuilder()
        .setSampler(Samplers.probabilitySampler(0.01))
        .build()
);
```

### Disable Bigtbale traces

If your application already has OpenCensus Tracing integration and you want to disable Bigtable
traces, you can do the following:

```java
public static class MySampler extends Sampler {

    private final Sampler childSampler;

    MySampler(Sampler child) {
        this.childSampler = child;
    }

    @Override
    public boolean shouldSample(@Nullable SpanContext parentContext,
                                @Nullable Boolean hasRemoteParent,
                                TraceId traceId,
                                SpanId spanId,
                                String name,
                                List<Span> parentLinks) {
        if (name.contains("Bigtable")) {
            return false;
        }
        return childSampler.shouldSample(parentContext, hasRemoteParent, traceId, spanId, name, parentLinks);
    }

    @Override
    public String getDescription() {
        return "from my sampler";
    }
}
```

And use this sampler in your trace config:
```java
Tracing.getTraceConfig().updateActiveTraceParams(
        Tracing.getTraceConfig().getActiveTraceParams().toBuilder()
                .setSampler(new MySampler(Samplers.probabilitySampler(0.1)))
                .build()
);
```

## Version Conflicts

google-cloud-bigtable depends on gRPC directly which may conflict with the versions brought
in by other libraries, for example Apache Beam. This happens because internal dependencies
between gRPC libraries are pinned to an exact version of grpc-core
(see [here](https://github.com/grpc/grpc-java/commit/90db93b990305aa5a8428cf391b55498c7993b6e)).
If both google-cloud-bigtable and the other library bring in two gRPC libraries that depend
on the different versions of grpc-core, then dependency resolution will fail.
The easiest way to fix this is to depend on the gRPC bom, which will force all the gRPC
transitive libraries to use the same version.

Add the following to your project's pom.xml.

```
    <dependencyManagement>
      <dependencies>
        <dependency>
          <groupId>io.grpc</groupId>
          <artifactId>grpc-bom</artifactId>
          <version>1.28.0</version>
          <type>pom</type>
          <scope>import</scope>
        </dependency>
      </dependencies>
    </dependencyManagement>
```

## Container Deployment

While deploying this client in [Google Kubernetes Engine(GKE)](https://cloud.google.com/kubernetes-engine) with [CoS](https://cloud.google.com/container-optimized-os/docs/). Please make sure to provide CPU configuration in your deployment file. With default configuration JVM detects only 1 CPU, which affects the number of channels with the client, resulting in performance repercussion.

Also, The number of `grpc-nio-worker-ELG-1-#` thread is same as number of CPUs. These are managed by a single `grpc-default-executor-#` thread, which is shared among multiple client instances.

For example:
```yaml
appVersion: v1
...
spec:
  ...
  container:
    resources:
      requests:
        cpu: "1" # Here 1 represents 100% of single node CPUs whereas other than 1 represents the number of CPU it would use from a node.
```
see [Assign CPU Resources to Containers](https://kubernetes.io/docs/tasks/configure-pod-container/assign-cpu-resource/#specify-a-cpu-request-and-a-cpu-limit) for more information.




## Samples

Samples are in the [`samples/`](https://github.com/googleapis/java-bigtable/tree/main/samples) directory.

| Sample                      | Source Code                       | Try it |
| --------------------------- | --------------------------------- | ------ |
| Native Image Bigtable Sample | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/native-image-sample/src/main/java/com/example/bigtable/NativeImageBigtableSample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/native-image-sample/src/main/java/com/example/bigtable/NativeImageBigtableSample.java) |
| Authorized View Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/AuthorizedViewExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/AuthorizedViewExample.java) |
| Configure Connection Pool | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/ConfigureConnectionPool.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/ConfigureConnectionPool.java) |
| Filters | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/Filters.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/Filters.java) |
| Hello World | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/HelloWorld.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/HelloWorld.java) |
| Instance Admin Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/InstanceAdminExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/InstanceAdminExample.java) |
| Key Salting | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/KeySalting.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/KeySalting.java) |
| Quickstart | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/Quickstart.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/Quickstart.java) |
| Reads | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/Reads.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/Reads.java) |
| Table Admin Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/TableAdminExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/TableAdminExample.java) |
| Write Aggregate | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/WriteAggregate.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/WriteAggregate.java) |
| Write Batch | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/WriteBatch.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/WriteBatch.java) |
| Write Conditionally | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/WriteConditionally.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/WriteConditionally.java) |
| Write Increment | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/WriteIncrement.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/WriteIncrement.java) |
| Write Simple | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/WriteSimple.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/WriteSimple.java) |
| Batch Delete Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/BatchDeleteExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/BatchDeleteExample.java) |
| Conditional Delete Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/ConditionalDeleteExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/ConditionalDeleteExample.java) |
| Delete Column Family Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteColumnFamilyExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteColumnFamilyExample.java) |
| Delete From Column Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteFromColumnExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteFromColumnExample.java) |
| Delete From Column Family Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteFromColumnFamilyExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteFromColumnFamilyExample.java) |
| Delete From Row Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteFromRowExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteFromRowExample.java) |
| Delete Table Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteTableExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/DeleteTableExample.java) |
| Drop Row Range Example | [source code](https://github.com/googleapis/java-bigtable/blob/main/samples/snippets/src/main/java/com/example/bigtable/deletes/DropRowRangeExample.java) | [![Open in Cloud Shell][shell_img]](https://console.cloud.google.com/cloudshell/open?git_repo=https://github.com/googleapis/java-bigtable&page=editor&open_in_editor=samples/snippets/src/main/java/com/example/bigtable/deletes/DropRowRangeExample.java) |



## Troubleshooting

To get help, follow the instructions in the [shared Troubleshooting document][troubleshooting].

## Supported Java Versions

Java 8 or above is required for using this client.

Google's Java client libraries,
[Google Cloud Client Libraries][cloudlibs]
and
[Google Cloud API Libraries][apilibs],
follow the
[Oracle Java SE support roadmap][oracle]
(see the Oracle Java SE Product Releases section).

### For new development

In general, new feature development occurs with support for the lowest Java
LTS version covered by  Oracle's Premier Support (which typically lasts 5 years
from initial General Availability). If the minimum required JVM for a given
library is changed, it is accompanied by a [semver][semver] major release.

Java 11 and (in September 2021) Java 17 are the best choices for new
development.

### Keeping production systems current

Google tests its client libraries with all current LTS versions covered by
Oracle's Extended Support (which typically lasts 8 years from initial
General Availability).

#### Legacy support

Google's client libraries support legacy versions of Java runtimes with long
term stable libraries that don't receive feature updates on a best efforts basis
as it may not be possible to backport all patches.

Google provides updates on a best efforts basis to apps that continue to use
Java 7, though apps might need to upgrade to current versions of the library
that supports their JVM.

#### Where to find specific information

The latest versions and the supported Java versions are identified on
the individual GitHub repository `github.com/GoogleAPIs/java-SERVICENAME`
and on [google-cloud-java][g-c-j].

## Versioning


This library follows [Semantic Versioning](http://semver.org/).



## Contributing


Contributions to this library are always welcome and highly encouraged.

See [CONTRIBUTING][contributing] for more information how to get started.

Please note that this project is released with a Contributor Code of Conduct. By participating in
this project you agree to abide by its terms. See [Code of Conduct][code-of-conduct] for more
information.


## License

Apache 2.0 - See [LICENSE][license] for more information.

## CI Status

Java Version | Status
------------ | ------
Java 8 | [![Kokoro CI][kokoro-badge-image-2]][kokoro-badge-link-2]
Java 8 OSX | [![Kokoro CI][kokoro-badge-image-3]][kokoro-badge-link-3]
Java 8 Windows | [![Kokoro CI][kokoro-badge-image-4]][kokoro-badge-link-4]
Java 11 | [![Kokoro CI][kokoro-badge-image-5]][kokoro-badge-link-5]

Java is a registered trademark of Oracle and/or its affiliates.

[product-docs]: https://cloud.google.com/bigtable
[javadocs]: https://cloud.google.com/java/docs/reference/google-cloud-bigtable/latest/history
[kokoro-badge-image-1]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java7.svg
[kokoro-badge-link-1]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java7.html
[kokoro-badge-image-2]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java8.svg
[kokoro-badge-link-2]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java8.html
[kokoro-badge-image-3]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java8-osx.svg
[kokoro-badge-link-3]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java8-osx.html
[kokoro-badge-image-4]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java8-win.svg
[kokoro-badge-link-4]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java8-win.html
[kokoro-badge-image-5]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java11.svg
[kokoro-badge-link-5]: http://storage.googleapis.com/cloud-devrel-public/java/badges/java-bigtable/java11.html
[stability-image]: https://img.shields.io/badge/stability-stable-green
[maven-version-image]: https://img.shields.io/maven-central/v/com.google.cloud/google-cloud-bigtable.svg
[maven-version-link]: https://central.sonatype.com/artifact/com.google.cloud/google-cloud-bigtable/2.39.5
[authentication]: https://github.com/googleapis/google-cloud-java#authentication
[auth-scopes]: https://developers.google.com/identity/protocols/oauth2/scopes
[predefined-iam-roles]: https://cloud.google.com/iam/docs/understanding-roles#predefined_roles
[iam-policy]: https://cloud.google.com/iam/docs/overview#cloud-iam-policy
[developer-console]: https://console.developers.google.com/
[create-project]: https://cloud.google.com/resource-manager/docs/creating-managing-projects
[cloud-cli]: https://cloud.google.com/cli
[troubleshooting]: https://github.com/googleapis/google-cloud-java/blob/main/TROUBLESHOOTING.md
[contributing]: https://github.com/googleapis/java-bigtable/blob/main/CONTRIBUTING.md
[code-of-conduct]: https://github.com/googleapis/java-bigtable/blob/main/CODE_OF_CONDUCT.md#contributor-code-of-conduct
[license]: https://github.com/googleapis/java-bigtable/blob/main/LICENSE

[enable-api]: https://console.cloud.google.com/flows/enableapi?apiid=bigtable.googleapis.com
[libraries-bom]: https://github.com/GoogleCloudPlatform/cloud-opensource-java/wiki/The-Google-Cloud-Platform-Libraries-BOM
[shell_img]: https://gstatic.com/cloudssh/images/open-btn.png

[semver]: https://semver.org/
[cloudlibs]: https://cloud.google.com/apis/docs/client-libraries-explained
[apilibs]: https://cloud.google.com/apis/docs/client-libraries-explained#google_api_client_libraries
[oracle]: https://www.oracle.com/java/technologies/java-se-support-roadmap.html
[g-c-j]: http://github.com/googleapis/google-cloud-java
