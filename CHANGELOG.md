# Changelog

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
