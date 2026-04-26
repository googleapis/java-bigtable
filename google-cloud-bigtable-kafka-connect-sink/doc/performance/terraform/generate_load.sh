#!/bin/bash
set -euo pipefail

CONFIG_FILE="$(mktemp)"
THROUGHPUT="$THROUGHPUT"
TIMEOUT="$TIMEOUT"
TOPIC="$TOPIC"
/opt/kafka/kafka_connect_config_generator.sh > "$CONFIG_FILE"

# https://opentelemetry.io/docs/zero-code/java/agent/getting-started/#configuring-the-agent
export JAVA_TOOL_OPTIONS="-javaagent:/opt/kafka/libs/opentelemetry-javaagent.jar"
# export OTEL_JAVAAGENT_DEBUG=true
export OTEL_EXPORTER_OTLP_PROTOCOL=grpc
kafka-console-generator | pv --line-mode --rate-limit "$THROUGHPUT" | timeout -v -s TERM -k "$TIMEOUT" "$TIMEOUT" /opt/kafka/bin/kafka-console-producer.sh \
    --producer.config "$CONFIG_FILE" \
    --topic "$TOPIC" \
    --bootstrap-server "$KAFKA_CONNECT_BOOTSTRAP_SERVERS" \
    --property parse.key=true \
    --property key.separator="|" \
    --compression-codec none \
    --request-required-acks 1 \
    --timeout 0 \
    || true
