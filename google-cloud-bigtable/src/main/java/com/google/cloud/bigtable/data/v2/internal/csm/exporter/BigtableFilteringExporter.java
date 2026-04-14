package com.google.cloud.bigtable.data.v2.internal.csm.exporter;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BigtableFilteringExporter implements MetricExporter {

  private MetricExporter delegate;
  private Predicate<MetricData> filter;

  public BigtableFilteringExporter(MetricExporter exporter, Predicate<MetricData> filter) {
    this.delegate = exporter;
    this.filter = filter;
  }

  @Override
  public CompletableResultCode export(Collection<MetricData> metrics) {
    List<MetricData> filtered = metrics.stream().filter(filter).collect(Collectors.toList());
    return delegate.export(filtered);
  }

  @Override
  public CompletableResultCode flush() {
    return delegate.flush();
  }

  @Override
  public CompletableResultCode shutdown() {
    return delegate.shutdown();
  }

  public void prepareForShutdown() {
    if (delegate instanceof BigtableCloudMonitoringExporter) {
      ((BigtableCloudMonitoringExporter) delegate).prepareForShutdown();
    }
  }

  @Override
  public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
    return delegate.getAggregationTemporality(instrumentType);
  }
}
