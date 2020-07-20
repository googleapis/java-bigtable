# Changelog

## [1.14.0](https://www.github.com/googleapis/java-bigtable/compare/v1.13.1...v1.14.0) (2020-07-20)


### Features

* Cloud Bigtable Managed Backup Implementation ([#305](https://www.github.com/googleapis/java-bigtable/issues/305)) ([9e3307a](https://www.github.com/googleapis/java-bigtable/commit/9e3307a4872d3bae3c04e7857a9eb4859151965e))

### [1.13.1](https://www.github.com/googleapis/java-bigtable/compare/v1.13.0...v1.13.1) (2020-07-10)


### Reverts

* Revert "fix: change app_profile_id to the correct header app_profile (#318)" (#335) ([200cfac](https://www.github.com/googleapis/java-bigtable/commit/200cfac9b68be9882943765b06de7c0644daf53d)), closes [#318](https://www.github.com/googleapis/java-bigtable/issues/318) [#335](https://www.github.com/googleapis/java-bigtable/issues/335)


### Dependencies

* update autovalue.version to v1.7.3 ([#341](https://www.github.com/googleapis/java-bigtable/issues/341)) ([071d0fe](https://www.github.com/googleapis/java-bigtable/commit/071d0feac03b5077b088a09244fe863aa111c231))
* update autovalue.version to v1.7.4 ([#365](https://www.github.com/googleapis/java-bigtable/issues/365)) ([18ba73d](https://www.github.com/googleapis/java-bigtable/commit/18ba73da3e6f1035699dc885109f90ffb160cb4e))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.6.0 ([#325](https://www.github.com/googleapis/java-bigtable/issues/325)) ([6fc5b7b](https://www.github.com/googleapis/java-bigtable/commit/6fc5b7b1f13c5326001cc7bcecbf4918a8505d26))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.7.0 ([#326](https://www.github.com/googleapis/java-bigtable/issues/326)) ([2aab7a8](https://www.github.com/googleapis/java-bigtable/commit/2aab7a8ec8775ab67e0277fd1785ee4735e1521d))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.1 ([#343](https://www.github.com/googleapis/java-bigtable/issues/343)) ([f0314a1](https://www.github.com/googleapis/java-bigtable/commit/f0314a13841cff495cc3424805a0aaf19bcc71d0))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.2 ([#362](https://www.github.com/googleapis/java-bigtable/issues/362)) ([1c37f5d](https://www.github.com/googleapis/java-bigtable/commit/1c37f5df35e4a317ac339e5d5b3cd50acf8e8c5a))
* update dependency com.google.cloud:google-cloud-shared-dependencies to v0.8.3 ([#366](https://www.github.com/googleapis/java-bigtable/issues/366)) ([ade8b36](https://www.github.com/googleapis/java-bigtable/commit/ade8b362d66378e6b1ba75c6c04558e4437080a9))
* update dependency com.google.errorprone:error_prone_annotations to v2.4.0 ([#328](https://www.github.com/googleapis/java-bigtable/issues/328)) ([b9e5e83](https://www.github.com/googleapis/java-bigtable/commit/b9e5e832791b728d5931e1fe4546d01ffb4903d1))
* update protobuf.version to v3.12.2 ([#320](https://www.github.com/googleapis/java-bigtable/issues/320)) ([d5cf1d4](https://www.github.com/googleapis/java-bigtable/commit/d5cf1d4a62f9782c18a2d7ebb48d137426f84d5b))
* use google-cloud-shared-dependencies in bigtable deps bom ([#324](https://www.github.com/googleapis/java-bigtable/issues/324)) ([8f76fc8](https://www.github.com/googleapis/java-bigtable/commit/8f76fc8e7d326ed7837070cf496a2f20d4c7485e))

## [1.13.0](https://www.github.com/googleapis/java-bigtable/compare/v1.12.2...v1.13.0) (2020-05-27)


### Features

* adding utility to transform protobuf into model object ([#299](https://www.github.com/googleapis/java-bigtable/issues/299)) ([00f6d2d](https://www.github.com/googleapis/java-bigtable/commit/00f6d2da4179eb3f4f55a1fe1da04047697c5999))
* expose new API with ReadRowsRequest in EnhancedBigtableStub ([#276](https://www.github.com/googleapis/java-bigtable/issues/276)) ([394efe4](https://www.github.com/googleapis/java-bigtable/commit/394efe459ebe34030bf1d09116eebb4ec4f311e9))
* Update opencensus metrics to include bigtable resource ids and rpc level metrics ([#214](https://www.github.com/googleapis/java-bigtable/issues/214)) ([7214ef6](https://www.github.com/googleapis/java-bigtable/commit/7214ef6853fc6892401b55bd1beeccbe896e4f33))


### Bug Fixes

* bigtable v2 retry config settings to disable streaming RPC retries ([#315](https://www.github.com/googleapis/java-bigtable/issues/315)) ([a7e43f0](https://www.github.com/googleapis/java-bigtable/commit/a7e43f07dc0b93c6702992d45444e815200bf202))
* change app_profile_id to the correct header app_profile ([#318](https://www.github.com/googleapis/java-bigtable/issues/318)) ([237b15d](https://www.github.com/googleapis/java-bigtable/commit/237b15d14b420005d32f227e1a76e7d308db0a42))


### Dependencies

* update autovalue.version to v1.7.2 ([#306](https://www.github.com/googleapis/java-bigtable/issues/306)) ([bc215c5](https://www.github.com/googleapis/java-bigtable/commit/bc215c5883b16727c28893b4a13b3387f0a04ac9))
* update dependency com.google.api:api-common to v1.9.1 ([#316](https://www.github.com/googleapis/java-bigtable/issues/316)) ([482603a](https://www.github.com/googleapis/java-bigtable/commit/482603acd4c3db461b0e5f489a8c55213b9f3326))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.11 ([#307](https://www.github.com/googleapis/java-bigtable/issues/307)) ([50a19a6](https://www.github.com/googleapis/java-bigtable/commit/50a19a6aded1b313390ff78b52d9ffc4f4cc7fdd))
* update dependency com.google.cloud:google-cloud-core-bom to v1.93.5 ([#317](https://www.github.com/googleapis/java-bigtable/issues/317)) ([7f61f90](https://www.github.com/googleapis/java-bigtable/commit/7f61f903abc16554c88850061faf03e6b8beb3b9))
* update protobuf.version to v3.12.1 ([#308](https://www.github.com/googleapis/java-bigtable/issues/308)) ([99e3a95](https://www.github.com/googleapis/java-bigtable/commit/99e3a95d456bb79841720e8ecbdcb0d47017535e))


### Documentation

* **fix:** update client documentation link ([#312](https://www.github.com/googleapis/java-bigtable/issues/312)) ([7e04c7a](https://www.github.com/googleapis/java-bigtable/commit/7e04c7a69a80d71303ce62b4320153facd3cfad8))

### [1.12.2](https://www.github.com/googleapis/java-bigtable/compare/v1.12.1...v1.12.2) (2020-05-11)


### Bug Fixes

* set x-goog-api-client as internal header ([#293](https://www.github.com/googleapis/java-bigtable/issues/293)) ([3b58a4f](https://www.github.com/googleapis/java-bigtable/commit/3b58a4fa82e7a1e003bf119ce3b8b52b6eb52e47))

### [1.12.1](https://www.github.com/googleapis/java-bigtable/compare/v1.12.0...v1.12.1) (2020-05-08)


### Bug Fixes

* add missing api-client header ([#290](https://www.github.com/googleapis/java-bigtable/issues/290)) ([656684b](https://www.github.com/googleapis/java-bigtable/commit/656684b7f0fff003bb582977beb828f83da9a135))

## [1.12.0](https://www.github.com/googleapis/java-bigtable/compare/v1.11.0...v1.12.0) (2020-05-06)


### Features

* add more context to row merging errors ([#281](https://www.github.com/googleapis/java-bigtable/issues/281)) ([d88547c](https://www.github.com/googleapis/java-bigtable/commit/d88547cb55e9e2df09471df62074165266847c6d))


### Bug Fixes

* **build:** fix version update flag in samples pom.xml ([#285](https://www.github.com/googleapis/java-bigtable/issues/285)) ([6a1f970](https://www.github.com/googleapis/java-bigtable/commit/6a1f9701574fcdd41bc8300115e21ebe31b6f426))


### Dependencies

* update autovalue.version to v1.7.1 ([#278](https://www.github.com/googleapis/java-bigtable/issues/278)) ([e2f4e9e](https://www.github.com/googleapis/java-bigtable/commit/e2f4e9e84277dd242bf2fc454ab3243ff557f1ca))
* update core dependencies ([#215](https://www.github.com/googleapis/java-bigtable/issues/215)) ([ad7aab4](https://www.github.com/googleapis/java-bigtable/commit/ad7aab435aa31541535e4e202f01a79484f007a5))
* update core dependencies for google-cloud-core and gax ([#240](https://www.github.com/googleapis/java-bigtable/issues/240)) ([8f384f4](https://www.github.com/googleapis/java-bigtable/commit/8f384f48e984c69b10a515d15324227b40130d83))
* update dependency com.google.api:api-common to v1.9.0 ([#231](https://www.github.com/googleapis/java-bigtable/issues/231)) ([9a0f983](https://www.github.com/googleapis/java-bigtable/commit/9a0f9838c9a96ae1108da36f79bf1a4cdf4b5749))
* update dependency com.google.api:gax-bom to v1.55.0 ([#234](https://www.github.com/googleapis/java-bigtable/issues/234)) ([f910a32](https://www.github.com/googleapis/java-bigtable/commit/f910a32abef5b24a093c1ecef71d47811c458b41))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.10 ([#271](https://www.github.com/googleapis/java-bigtable/issues/271)) ([2bf6195](https://www.github.com/googleapis/java-bigtable/commit/2bf6195e8c215b8a6a72b66711fb24e98f3ab5a2))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.8 ([#216](https://www.github.com/googleapis/java-bigtable/issues/216)) ([423cb6b](https://www.github.com/googleapis/java-bigtable/commit/423cb6b395aa67986993f7d6c9dbd588e93faca6))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.9 ([#230](https://www.github.com/googleapis/java-bigtable/issues/230)) ([a84c689](https://www.github.com/googleapis/java-bigtable/commit/a84c689150990fe39ccc8055941ffb237cd499f5))
* update dependency com.google.cloud:google-cloud-core-bom to v1.93.3 ([#219](https://www.github.com/googleapis/java-bigtable/issues/219)) ([92f2755](https://www.github.com/googleapis/java-bigtable/commit/92f2755ca3b5218daa286556ce2c8b2f5b79fad6))
* update dependency com.google.cloud.samples:shared-configuration to v1.0.15 ([#246](https://www.github.com/googleapis/java-bigtable/issues/246)) ([3348210](https://www.github.com/googleapis/java-bigtable/commit/33482104496856dc849fe9858c429266a6caf4da))
* update dependency com.google.guava:guava-bom to v29 ([#244](https://www.github.com/googleapis/java-bigtable/issues/244)) ([0f7bd45](https://www.github.com/googleapis/java-bigtable/commit/0f7bd45bf15ebe25e2b5dd75134885e43b2604f0))
* update dependency com.google.http-client:google-http-client-bom to v1.35.0 ([#272](https://www.github.com/googleapis/java-bigtable/issues/272)) ([af0874b](https://www.github.com/googleapis/java-bigtable/commit/af0874bcbab22366514b89d8e567cbeeed4789f8))
* update dependency io.grpc:grpc-bom to v1.28.1 ([#236](https://www.github.com/googleapis/java-bigtable/issues/236)) ([4a72205](https://www.github.com/googleapis/java-bigtable/commit/4a72205018b39d71cfa54466fe44630c0e4202aa))
* update dependency io.grpc:grpc-bom to v1.29.0 ([#267](https://www.github.com/googleapis/java-bigtable/issues/267)) ([2e6393b](https://www.github.com/googleapis/java-bigtable/commit/2e6393b2bf4899ab603dfc6de9128df76d4ecb2d))
* update dependency org.threeten:threetenbp to v1.4.2 ([#225](https://www.github.com/googleapis/java-bigtable/issues/225)) ([5b94b02](https://www.github.com/googleapis/java-bigtable/commit/5b94b02ddc53c98d4a59e5457bdecc949d177c84))
* update dependency org.threeten:threetenbp to v1.4.3 ([#237](https://www.github.com/googleapis/java-bigtable/issues/237)) ([86fab5c](https://www.github.com/googleapis/java-bigtable/commit/86fab5cb90b1ca83c494f6787ca9d10d930ca1ff))
* update dependency org.threeten:threetenbp to v1.4.4 ([#270](https://www.github.com/googleapis/java-bigtable/issues/270)) ([6eba2af](https://www.github.com/googleapis/java-bigtable/commit/6eba2aff251fd3930a971d6270f297d449f21320))
* update google.common-protos.version to v1.18.0 ([#279](https://www.github.com/googleapis/java-bigtable/issues/279)) ([1571dd9](https://www.github.com/googleapis/java-bigtable/commit/1571dd939c24484309b8f484f73f29e602fce27c))
* update opencensus.version to v0.26.0 ([#223](https://www.github.com/googleapis/java-bigtable/issues/223)) ([1a00982](https://www.github.com/googleapis/java-bigtable/commit/1a00982eead27d3e3b13bc0c3942459618408e53))

## [1.11.0](https://www.github.com/googleapis/java-bigtable/compare/v1.10.0...v1.11.0) (2020-03-03)


### Features

* add row exists api ([#190](https://www.github.com/googleapis/java-bigtable/issues/190)) ([d141c3d](https://www.github.com/googleapis/java-bigtable/commit/d141c3d597cbd682050b78bb3828fd4d8c96a7c3))


### Dependencies

* update core dependencies ([#183](https://www.github.com/googleapis/java-bigtable/issues/183)) ([674bb73](https://www.github.com/googleapis/java-bigtable/commit/674bb73fa8a37233ba8f8d336e6e4be72a91f3a0))
* update dependency com.google.api:gax-bom to v1.54.0 ([#208](https://www.github.com/googleapis/java-bigtable/issues/208)) ([16cb625](https://www.github.com/googleapis/java-bigtable/commit/16cb62576058cb4124b98445f90b1afed012fd86))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.5 ([#180](https://www.github.com/googleapis/java-bigtable/issues/180)) ([687335b](https://www.github.com/googleapis/java-bigtable/commit/687335b97c1db121a5aa2681bbddd9c0b733d9c4))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.6 ([#207](https://www.github.com/googleapis/java-bigtable/issues/207)) ([6e803e9](https://www.github.com/googleapis/java-bigtable/commit/6e803e9ffdf70ace27d31caf3ba428a9017c1799))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.7 ([#210](https://www.github.com/googleapis/java-bigtable/issues/210)) ([9988811](https://www.github.com/googleapis/java-bigtable/commit/9988811e32eecee357c81beecab4199708b700fa))
* update dependency com.google.cloud:google-cloud-core-bom to v1.92.3 ([#179](https://www.github.com/googleapis/java-bigtable/issues/179)) ([7b896dd](https://www.github.com/googleapis/java-bigtable/commit/7b896dd60eb44e14d862789e9f8a15c113a235c8))
* update dependency com.google.cloud:google-cloud-core-bom to v1.92.5 ([279b64c](https://www.github.com/googleapis/java-bigtable/commit/279b64ca03da9e3f42e1468ea4f2b0a28f4ac939))
* update dependency com.google.cloud:google-cloud-core-bom to v1.93.0 ([#209](https://www.github.com/googleapis/java-bigtable/issues/209)) ([86905bf](https://www.github.com/googleapis/java-bigtable/commit/86905bfe12e7ea251c3eafafe328f87d7687bf4e))
* update dependency com.google.cloud:google-cloud-core-bom to v1.93.1 ([#211](https://www.github.com/googleapis/java-bigtable/issues/211)) ([a4596b5](https://www.github.com/googleapis/java-bigtable/commit/a4596b5db8d79bd86dfa33c7ae280a7227928398))
* update dependency com.google.http-client:google-http-client-bom to v1.34.1 ([#175](https://www.github.com/googleapis/java-bigtable/issues/175)) ([c25e8aa](https://www.github.com/googleapis/java-bigtable/commit/c25e8aa509796c33af4c53ffb68d7e40e6dbc237))
* update dependency com.google.http-client:google-http-client-bom to v1.34.2 ([#201](https://www.github.com/googleapis/java-bigtable/issues/201)) ([72c6d52](https://www.github.com/googleapis/java-bigtable/commit/72c6d527ce52d3fcc4f8340dcb2ded6035993260))
* update dependency io.grpc:grpc-bom to v1.27.0 ([#177](https://www.github.com/googleapis/java-bigtable/issues/177)) ([c8ab8a6](https://www.github.com/googleapis/java-bigtable/commit/c8ab8a6f94d7017f0ab0c7e2d23b99ba4388afd4))
* update dependency io.grpc:grpc-bom to v1.27.1 ([#200](https://www.github.com/googleapis/java-bigtable/issues/200)) ([c622be3](https://www.github.com/googleapis/java-bigtable/commit/c622be3fb3e2034b3834c38881015b04f01c2859))
* update dependency io.grpc:grpc-bom to v1.27.2 ([#206](https://www.github.com/googleapis/java-bigtable/issues/206)) ([33a609a](https://www.github.com/googleapis/java-bigtable/commit/33a609a0cdc32cdcbf42b04597a476b144849bce))
* update opencensus.version to v0.25.0 ([#198](https://www.github.com/googleapis/java-bigtable/issues/198)) ([1a36da2](https://www.github.com/googleapis/java-bigtable/commit/1a36da2c7e397650e6cac3172b9a9fcc953796ff))
* update protobuf.version to v3.11.3 ([#187](https://www.github.com/googleapis/java-bigtable/issues/187)) ([f38e3e3](https://www.github.com/googleapis/java-bigtable/commit/f38e3e37a51121b55faf91737bbb3cee36baa23a))
* update protobuf.version to v3.11.4 ([#202](https://www.github.com/googleapis/java-bigtable/issues/202)) ([f9b4438](https://www.github.com/googleapis/java-bigtable/commit/f9b44388cdb32af12314a00ab420278e29969d76))


### Documentation

* **regen:** update sample code to set total timeout, add API client header test ([f3be131](https://www.github.com/googleapis/java-bigtable/commit/f3be131923a86359be541b53627b15bda8ed407e))
* readme note for gke deployment configuration ([#185](https://www.github.com/googleapis/java-bigtable/issues/185)) ([0afa13a](https://www.github.com/googleapis/java-bigtable/commit/0afa13ae8df95119acc482ec79e204abd1bc333e))

## [1.10.0](https://www.github.com/googleapis/java-bigtable/compare/v1.9.1...v1.10.0) (2020-01-27)


### Features

* introducing bulk read API through Batcher ([#99](https://www.github.com/googleapis/java-bigtable/issues/99)) ([e87179e](https://www.github.com/googleapis/java-bigtable/commit/e87179ebe1e53b7962a940a9aba761da8047e7e4))


### Dependencies

* update dependency com.google.auth:google-auth-library-bom to v0.20.0 ([#168](https://www.github.com/googleapis/java-bigtable/issues/168)) ([8d69936](https://www.github.com/googleapis/java-bigtable/commit/8d69936e0d3cdba0085b363852e85fcb9eeb9dab))
* update dependency com.google.cloud:google-cloud-conformance-tests to v0.0.4 ([#157](https://www.github.com/googleapis/java-bigtable/issues/157)) ([abbc6c8](https://www.github.com/googleapis/java-bigtable/commit/abbc6c8c0ffbe1dcf88c14b1e70fe3150c61c582))
* update dependency com.google.cloud:google-cloud-core-bom to v1.92.2 ([#147](https://www.github.com/googleapis/java-bigtable/issues/147)) ([f3462b1](https://www.github.com/googleapis/java-bigtable/commit/f3462b119aa154f116e55f95e25171c482e08f8a))
* update dependency com.google.truth:truth to v1.0.1 ([#156](https://www.github.com/googleapis/java-bigtable/issues/156)) ([f767feb](https://www.github.com/googleapis/java-bigtable/commit/f767febcd3bf913bbb2e5421fa716a13a7b4a33b))
* update dependency org.threeten:threetenbp to v1.4.1 ([#153](https://www.github.com/googleapis/java-bigtable/issues/153)) ([f92da32](https://www.github.com/googleapis/java-bigtable/commit/f92da32e4834fe44148000f127b2962ecb94a67a))


### Documentation

* synth updates to javadoc and kokoro build configs ([d6275a7](https://www.github.com/googleapis/java-bigtable/commit/d6275a7877d98f5794ec7d400c10b2dce89167f0))

### [1.9.1](https://www.github.com/googleapis/java-bigtable/compare/v1.9.0...v1.9.1) (2020-01-09)


### Dependencies

* upgrade gax to 1.53.0 ([#145](https://www.github.com/googleapis/java-bigtable/issues/145)) ([c3fc40d](https://www.github.com/googleapis/java-bigtable/commit/c3fc40df4358bd68cddcfc6f0a3fedb74dcdf465))

## [1.9.0](https://www.github.com/googleapis/java-bigtable/compare/v1.8.0...v1.9.0) (2020-01-06)


### Features

* add BigtableDataClientFactory to create lightweight data clients ([#112](https://www.github.com/googleapis/java-bigtable/issues/112)) ([d6bfd30](https://www.github.com/googleapis/java-bigtable/commit/d6bfd30ea9484b7433a048bdaad66153ffaa6a79))
* introduce google-cloud-bigtable-deps-bom ([#130](https://www.github.com/googleapis/java-bigtable/issues/130)) ([9289a13](https://www.github.com/googleapis/java-bigtable/commit/9289a13203b009930507c858ed14326768174c28))


### Dependencies

* update core dependencies to v1.92.1 ([#132](https://www.github.com/googleapis/java-bigtable/issues/132)) ([da1e6c6](https://www.github.com/googleapis/java-bigtable/commit/da1e6c61770c934644588f8a9933f6f68bd35ad1))
* update dependency com.google.guava:guava-bom to v28.2-android ([2905528](https://www.github.com/googleapis/java-bigtable/commit/2905528eac8f39e2aa39ca5bca13c6aba33f178b))
* update dependency io.grpc:grpc-bom to v1.26.0 ([#121](https://www.github.com/googleapis/java-bigtable/issues/121)) ([82a5094](https://www.github.com/googleapis/java-bigtable/commit/82a50943e454a3e8a23f002128d842eb98458b51))
* update dependency junit:junit to v4.13 ([#131](https://www.github.com/googleapis/java-bigtable/issues/131)) ([d4f46fd](https://www.github.com/googleapis/java-bigtable/commit/d4f46fd7118f2037a58bae2c467ff11449ee4dfc))


### Documentation

* update README to include CI Status ([#125](https://www.github.com/googleapis/java-bigtable/issues/125)) ([1e4987b](https://www.github.com/googleapis/java-bigtable/commit/1e4987b8baaa7e0dfd3905e6acf643788d5c722c))

## [1.8.0](https://www.github.com/googleapis/java-bigtable/compare/v1.7.1...v1.8.0) (2019-12-17)


### Features

* add implementation of ChannelPrimer to establish connection to GFE and integrate into bigtable client ([#115](https://www.github.com/googleapis/java-bigtable/issues/115)) ([276f942](https://www.github.com/googleapis/java-bigtable/commit/276f942dd2e668600347a59496525a589d7560da))
* update filter to accept an exact timestamp for better accessibility ([#92](https://www.github.com/googleapis/java-bigtable/issues/92)) ([e25758b](https://www.github.com/googleapis/java-bigtable/commit/e25758b3164618ac16d57fffe6ba0e4175229960))


### Dependencies

* update gax.version to v1.51.0 ([#105](https://www.github.com/googleapis/java-bigtable/issues/105)) ([dcdd0c3](https://www.github.com/googleapis/java-bigtable/commit/dcdd0c347f10802be57c4f7267c2b82c59ea2278))
* upgrade gax to 1.52.0, google-cloud-core to 1.92.0 ([#118](https://www.github.com/googleapis/java-bigtable/issues/118)) ([c106497](https://www.github.com/googleapis/java-bigtable/commit/c1064977eff3889e4f8b6ba8ab44a08f6f96ae1f))

### [1.7.1](https://www.github.com/googleapis/java-bigtable/compare/v1.7.0...v1.7.1) (2019-11-13)


### Dependencies

* update gax.version to v1.50.1 ([#72](https://www.github.com/googleapis/java-bigtable/issues/72)) ([eb44a19](https://www.github.com/googleapis/java-bigtable/commit/eb44a19ccaed342b84dec80048802641dfd6609e))

## [1.7.0](https://www.github.com/googleapis/java-bigtable/compare/v1.6.0...v1.7.0) (2019-11-07)


### Features

* add bom ([#50](https://www.github.com/googleapis/java-bigtable/issues/50)) ([f4dd552](https://www.github.com/googleapis/java-bigtable/commit/f4dd552ea00044babe1273e322b8a330f093b2b0))


### Bug Fixes

* align version numbers for proto and grpc versions ([#52](https://www.github.com/googleapis/java-bigtable/issues/52)) ([dbfa73a](https://www.github.com/googleapis/java-bigtable/commit/dbfa73a1a336d0afb03e7755d17786216199a851))
* fix maven test configs to make sure admin tests don't clobber data tests ([#47](https://www.github.com/googleapis/java-bigtable/issues/47)) ([18576f5](https://www.github.com/googleapis/java-bigtable/commit/18576f55c34dbdc4c0ac79639dbc1dbf1ce7affe))
* fix regression in batch#close silently ignoring entry failures by upgrading to gax to 1.50.0 ([#67](https://www.github.com/googleapis/java-bigtable/issues/67)) ([5aa8769](https://www.github.com/googleapis/java-bigtable/commit/5aa87697a5e8860993a14f4ac42d675c66b3d0ff))
* handle recovery failures during stream reframing failure ([#46](https://www.github.com/googleapis/java-bigtable/issues/46)) ([a16cb88](https://www.github.com/googleapis/java-bigtable/commit/a16cb8864c0c8be26d34e71dbf261dbfc5e09bac))
* Prevent integration tests from different profiles from trampling each other ([#69](https://www.github.com/googleapis/java-bigtable/issues/69)) ([638615a](https://www.github.com/googleapis/java-bigtable/commit/638615ae09ec2d311e82d89ea7a78137911f4eb4))
* use proper scope for DirectPath transitive dependencies ([#59](https://www.github.com/googleapis/java-bigtable/issues/59)) ([1d1c4de](https://www.github.com/googleapis/java-bigtable/commit/1d1c4deb34a85836da2aed7b07359ff923b09835))


### Documentation

* cleanup links to java-bigtable and javadoc ([#56](https://www.github.com/googleapis/java-bigtable/issues/56)) ([846a44f](https://www.github.com/googleapis/java-bigtable/commit/846a44fbf9ebd5691a431e79a09a049aea5fd4f0))

## [1.6.0](https://www.github.com/googleapis/java-bigtable/compare/1.5.0...v1.6.0) (2019-10-24)


### Features

* Add Experimental DirectPath support ([#8](https://www.github.com/googleapis/java-bigtable/issues/8)) ([2dd5105](https://www.github.com/googleapis/java-bigtable/commit/2dd5105559f6f61f9780b11e44bccbd2c08d68ae))


### Dependencies

* update gax.version to v1.49.1 ([a138a0f](https://www.github.com/googleapis/java-bigtable/commit/a138a0f8f42ede72d5cd30536cef4e78e5de5843))
* upgrade threeten to 1.4 ([#33](https://www.github.com/googleapis/java-bigtable/issues/33)) ([554170b](https://www.github.com/googleapis/java-bigtable/commit/554170b929ec63155d0e840039e75b323dad1709))
