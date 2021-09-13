# Copyright 2018 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""This script is used to synthesize generated parts of this library."""

import re
import synthtool as s
import synthtool.gcp as gcp
import synthtool.languages.java as java

data_excludes = [
    "build.gradle",
    "pom.xml",
    "README.md",
    "src/main/java/com/google/cloud/bigtable/data/v2/package-info.java",
    "src/main/java/com/google/cloud/bigtable/data/v2/BaseBigtableDataClient.java",
    "src/main/java/com/google/cloud/bigtable/data/v2/BaseBigtableDataSettings.java",
    "src/test/java/com/google/cloud/bigtable/data/v2/BaseBigtableDataClientTest.java",
    "src/test/java/com/google/cloud/bigtable/data/v2/MockBigtable.java",
    "src/test/java/com/google/cloud/bigtable/data/v2/MockBigtableImpl.java",
]

# Paths are relative to the destination, which is the current working directory
data_internal_only = [
    "google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/data/v2/stub/BigtableStub.java",
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/data/v2/stub/BigtableStubSettings.java',
    "google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/data/v2/stub/GrpcBigtableStub.java",
    "google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/data/v2/stub/GrpcBigtableCallableFactory.java",
]

# Excludes are relative to source `gapic-google-cloud-bigtable-v2/src`
admin_excludes = [
    "main/java/com/google/cloud/bigtable/admin/v2/package-info.java",
]

# Paths are relative to the destination, which is the current working directory
admin_internal_only = [
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/stub/GrpcBigtableInstanceAdminCallableFactory.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/stub/GrpcBigtableInstanceAdminStub.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableInstanceAdminSettings.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/stub/GrpcBigtableTableAdminCallableFactory.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/stub/GrpcBigtableTableAdminStub.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableTableAdminSettings.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/stub/BigtableInstanceAdminStub.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/stub/BigtableTableAdminStub.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableInstanceAdminClient.java',
    'google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableTableAdminClient.java',
]



# TODO: try to move the GAPIC surface hiding to the gapic-generator:
# https://github.com/googleapis/gapic-generator/issues/2742
def make_internal_only(sources):
  """Annotates all toplevel classes with `@InternalOnly`"""
  # Insert InternalOnly import in the non-static.
  s.replace(sources,
            before=r'^(import (?!static).*?\n)\n',
            after=r'\1import com.google.api.core.InternalApi;\n\n',
            flags=re.MULTILINE | re.DOTALL)

  # Replace javadoc and annotations before every public class with InternalApi
  # javadoc and annotation
  s.replace(sources,
            before=r'/\*\*.+?\*/\n(?:^@[^\n]*\n)*(?=public [a-zA-B ]*class)',
            after='/** For internal use only. */\n@Generated("by gapic-generator")\n@InternalApi\n',
            flags=re.MULTILINE | re.DOTALL)


for library in s.get_staging_dirs():
  # put any special-case replacements here
  make_internal_only(data_internal_only)

  # Generate admin client
  #todo: fix in synthtool removing comments with method
  java.remove_method(f'owl-bot-staging/v2/grpc-google-cloud-bigtable-admin-v2/src/main/java/com/google/bigtable/admin/v2/BigtableInstanceAdminGrpc.java', "public UnaryCallSettings<Instance, Instance> updateInstanceSettings()")
  java.remove_method(f'owl-bot-staging/v2/google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableInstanceAdminSettings.java', "/** Returns the builder for the settings used for calls to updateInstance. */")
  java.remove_method(f'owl-bot-staging/v2/google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableInstanceAdminSettings.java', "public UnaryCallSettings.Builder<Instance, Instance> updateInstanceSettings()")
  java.remove_method(f'owl-bot-staging/v2/google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableInstanceAdminSettings.java', "/** Returns the object with the settings used for calls to updateInstance. */")
  java.remove_method(f'owl-bot-staging/v2/google-cloud-bigtable/src/main/java/com/google/cloud/bigtable/admin/v2/BaseBigtableInstanceAdminSettings.java', "public UnaryCallSettings<Instance, Instance> updateInstanceSettings()")
  make_internal_only(admin_internal_only)

s.remove_staging_dirs()

java.common_templates(excludes=[
    '.gitignore',
    '.kokoro/presubmit/integration.cfg',
    '.kokoro/nightly/integration.cfg',
    '.kokoro/presubmit/samples.cfg',
    '.kokoro/nightly/samples.cfg',
    # todo remove once template is updated
    '.github/ISSUE_TEMPLATE/bug_report.md',
    'CONTRIBUTING.md',
    # exclude autogen
    'codecov.yaml'
    # needed for extraFiles
    '.github/release-please.yml',
])
