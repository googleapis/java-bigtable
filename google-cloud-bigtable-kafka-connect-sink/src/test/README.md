# Running the tests
All the commands are to be run in the directory containing the sink's [pom.xml](../../pom.xml).

## Unit tests
The unit test runner is [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/).

To run the test execute the following command:
```bash
mvn clean test
```

## Integration tests
The unit test runner is [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/).

### Prerequirements
The integration tests need a reachable Bigtable instance.
It might be either a real Cloud Bigtable or the emulator.
Note that some of the tests are broken on the emulator (because it handles some requests differently than Cloud Bigtable).

To configure the Bigtable instance for tests, create either a real Cloud Bigtable instance, or an emulator instance:

#### Cloud Bigtable
##### Create a Cloud Bigtable instance
It can be either created using either the [WebUI form](https://console.cloud.google.com/bigtable/create-instance) or terraform `google_bigtable_instance` resource, for example:
```terraform
resource "google_bigtable_instance" "bigtable" {
  name                = "kafka-connect-bigtable-sink-test"
  deletion_protection = false

  cluster {
    cluster_id   = "kafka-connect-bigtable-sink-test-cluster"
    num_nodes    = 1
    storage_type = "HDD"
    zone         = "europe-central2-a"
  }
}
```

##### [optional] Create Service Account with required permissions
This section is optional, you can skip it if you want to use Application Default Credentials.

Create a service account and grant it Bigtable Administrator (`roles/bigtable.admin`) permissions (such wide permissions are needed for table and column family auto creation).

##### Configure the integration tests to use the created instance
Ensure that the sink's [pom.xml](../../pom.xml) does **not** contain the following section in Failsafe's `<configuration>` section:
```xml
<environmentVariables>
	<GOOGLE_APPLICATION_CREDENTIALS>target/test-classes/fake_service_key.json</GOOGLE_APPLICATION_CREDENTIALS>
	<BIGTABLE_EMULATOR_HOST>localhost:8086</BIGTABLE_EMULATOR_HOST>
</environmentVariables>
```

<!-- TODO: update this section when transitioning to kokoro -->
Replace the following TODO values with you GCP project ID and Cloud Bigtable instance ID in `BaseIT#baseConnectorProps()` function:
```java
result.put(GCP_PROJECT_ID_CONFIG, "todotodo");
result.put(BIGTABLE_INSTANCE_ID_CONFIG, "todotodo");
```

##### [optional] Configure the permissions for integration tests
<!-- TODO: update this section when transitioning to kokoro -->
If you want to use Application Default Credentials, configure the machine (on a workstation, log in with `gcloud` into an account with Bigtable Administrator permissions to the instance created in one of the previous steps).

Otherwise, you need to use service account's permissions:
- download a service account key.
- put that key or path to it to the properties in `BaseIT#baseConnectorProps()` with a key defined in `BigtableSinkConfig`.

#### Emulator
Start the emulator using `gcloud` directly:
```bash
gcloud beta emulators bigtable start --host-port=127.0.0.1:8086 &
```
or using Docker with compose plugin:
```yaml
services:
  bigtable:
    image: google/cloud-sdk:latest
    ports:
      - 127.0.0.1:8086:8086
    entrypoint:
      - gcloud
      - beta
      - emulators
      - bigtable
      - start
      - --host-port=0.0.0.0:8086
```
```bash
docker compose up -d
```

Ensure that the sink's [pom.xml](../../pom.xml) contains the following section in Failsafe's `<configuration>` section:
```xml
<environmentVariables>
	<GOOGLE_APPLICATION_CREDENTIALS>target/test-classes/fake_service_key.json</GOOGLE_APPLICATION_CREDENTIALS>
	<BIGTABLE_EMULATOR_HOST>localhost:8086</BIGTABLE_EMULATOR_HOST>
</environmentVariables>
```

### Assumptions
The integration tests assume that the Bigtable instance they use is empty at the start of the run.
The assumption is used to skip cleaning up the tables created by the tests.

If the limit on number of tables in a single Cloud Bigtable instance starts causing problems for you, clean them up by running:
```bash
PROJECT=<FILL_YOUR_PROJECT_NAME>
INSTANCE=<FILL_YOUR_INSTANCE_NAME>
cbt -project "$PROJECT" -instance "$INSTANCE" ls | xargs -P 0 -I {} cbt -project "$PROJECT" -instance "$INSTANCE" {} 
```

### Command to run the integration tests
```bash
mvn clean integration-tests -DskipUnitTests
```
