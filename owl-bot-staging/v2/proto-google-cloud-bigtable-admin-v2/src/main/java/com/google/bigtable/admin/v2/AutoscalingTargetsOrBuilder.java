// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/instance.proto

package com.google.bigtable.admin.v2;

public interface AutoscalingTargetsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.AutoscalingTargets)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The cpu utilization that the Autoscaler should be trying to achieve.
   * This number is on a scale from 0 (no utilization) to
   * 100 (total utilization), and is limited between 10 and 80, otherwise it
   * will return INVALID_ARGUMENT error.
   * </pre>
   *
   * <code>int32 cpu_utilization_percent = 2;</code>
   * @return The cpuUtilizationPercent.
   */
  int getCpuUtilizationPercent();
}
