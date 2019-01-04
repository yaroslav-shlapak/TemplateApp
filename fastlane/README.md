fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew cask install fastlane`

# Available Actions
## Android
### android custom
```
fastlane android custom
```
Run some custom sequence of lanes, supposed to be long and performed on CI server
### android test
```
fastlane android test
```
Runs all the tests
### android unitTest
```
fastlane android unitTest
```
Runs all unit tests
### android integrationTest
```
fastlane android integrationTest
```
Runs all integration tests
### android gradleClean
```
fastlane android gradleClean
```
Gradle clean
### android beta
```
fastlane android beta
```
Submit a new Beta Build to Crashlytics Beta
### android publishInternal
```
fastlane android publishInternal
```
Deploy release bundle to the Google Play internal track
### android publishBeta
```
fastlane android publishBeta
```
Deploy release bundle to the Google Play beta track
### android buildRelease
```
fastlane android buildRelease
```
Build release bundle

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
