package com.google.cloud.bigtable.data.v2.stub;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CpuThrottlingStats {
  private long client_wait;
  private static AtomicLong timesRequested;
  private long lastQpsUpdateTime;

  public CpuThrottlingStats() {
    timesRequested = new AtomicLong();
  }

  public long getlastQpsUpdateTime() {
    return lastQpsUpdateTime;
  }

  public long getTimesChanges() {
    return timesRequested.get();
  }

  public void addTimesChanged() {
    System.out.println("Adding time");
    System.out.println(timesRequested.addAndGet(1));
  }

  // Decaying average?
}
