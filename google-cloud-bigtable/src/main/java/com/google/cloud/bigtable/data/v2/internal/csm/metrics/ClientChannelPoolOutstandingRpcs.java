package com.google.cloud.bigtable.data.v2.internal.csm.metrics;

import com.google.bigtable.v2.PeerInfo.TransportType;
import com.google.cloud.bigtable.data.v2.internal.csm.attributes.ClientInfo;
import com.google.cloud.bigtable.data.v2.internal.csm.attributes.Util;
import com.google.cloud.bigtable.data.v2.internal.csm.metrics.Constants.MetricLabels;
import com.google.cloud.bigtable.data.v2.internal.csm.schema.ClientSchema;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPoolSettings.LoadBalancingStrategy;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;

public class ClientChannelPoolOutstandingRpcs extends MetricWrapper<ClientSchema> {
  private static final String NAME =
      "bigtable.googleapis.com/internal/client/connection_pool/outstanding_rpcs";

  public ClientChannelPoolOutstandingRpcs() {
    super(ClientSchema.INSTANCE, NAME);
  }

  public Recorder newRecorder(Meter meter) {
    return new Recorder(meter);
  }

  public class Recorder {
    private final LongHistogram instrument;

    private Recorder(Meter meter) {
      this.instrument =
          meter
              .histogramBuilder(NAME)
              .ofLongs()
              .setDescription(
                  "A distribution of the number of outstanding RPCs per connection in the client"
                      + " pool, sampled periodically.")
              .setUnit("1")
              .build();
    }

    public void record(
        ClientInfo clientInfo,
        TransportType transportType,
        LoadBalancingStrategy lbPolicy,
        boolean isStreaming,
        long rpcCount) {
      instrument.record(
          rpcCount,
          getSchema()
              .createResourceAttrs(clientInfo)
              .put(MetricLabels.TRANSPORT_TYPE, Util.transportTypeToString(transportType))
              .put(MetricLabels.CHANNEL_POOL_LB_POLICY, lbPolicy.name())
              .put(MetricLabels.STREAMING_KEY, isStreaming)
              .build());
    }
  }
}
