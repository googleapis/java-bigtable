package com.google.cloud.bigtable.data.v2.stub;

import static com.google.common.truth.Truth.assertAbout;

import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import io.grpc.Metadata;
import javax.annotation.Nullable;

/** Utility class to test key-value pairs in {@link io.grpc.Metadata}. */
final class MetadataSubject extends Subject {

  @Nullable private final Metadata actual;

  public static Factory<MetadataSubject, Metadata> metadata() {
    return MetadataSubject::new;
  }

  private MetadataSubject(FailureMetadata metadata, @Nullable Metadata actual) {
    super(metadata, actual);
    this.actual = actual;
  }

  public static MetadataSubject assertThat(@Nullable Metadata actual) {
    return assertAbout(metadata()).that(actual);
  }

  public void containsAtLeast(String... keyValuePairs) {
    assert actual != null;
    for (int i = 0; i < keyValuePairs.length; i += 2) {
      check("containsAtLeast()")
          .that(actual.get(Metadata.Key.of(keyValuePairs[i], Metadata.ASCII_STRING_MARSHALLER)))
          .isEqualTo(keyValuePairs[i + 1]);
    }
  }

  public void doesNotContainKeys(String... keys) {
    assert actual != null;
    for (String key : keys) {
      check("doesNotContainKeys()")
          .that(actual.containsKey(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)))
          .isFalse();
    }
  }
}
