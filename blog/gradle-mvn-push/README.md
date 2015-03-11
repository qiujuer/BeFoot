gradle-mvn-push
===============

See this blog post for more context on this 'library': [http://blog.csdn.net/qiujuer/article/details/44195131](http://blog.csdn.net/qiujuer/article/details/44195131).


## Usage

### 1. Have a working Gradle build
This is up to you.

### 2. Update your home gradle.properties

See `gradle-home.properties`
This will include the username and password to upload to the Maven server and so that they are kept local on your machine. The location defaults to `USER_HOME/.gradle/gradle.properties`.

It may also include your signing key id, password, and secret key ring file (for signed uploads).  Signing is only necessary if you're putting release builds of your project on maven central.

```properties
signing.keyId=766BDH54
signing.password=GPGSIGNINGKEY
signing.secretKeyRingFile=D:/.gnupg/secring.gpg

MAVEN_USERNAME=qiujuer
MAVEN_PASSWORD=Password
```

### 3. Create project root gradle.properties

See `gradle-root.properties`
You may already have this file, in which case just edit the original. This file should contain the POM values which are common to all of your sub-project (if you have any). For instance, here's [Genius-Android](https://github.com/qiujuer/Genius-Android):

```properties
POM_GROUP_ID=com.github.qiujuer
POM_DESCRIPTION=Genius Lib For Android

POM_URL=https://github.com/qiujuer/Genius-Android

POM_SCM_URL=https://github.com/qiujuer/Genius-Android
POM_SCM_CONNECTION=scm:git@github.com:qiujuer/Genius-Android.git
POM_SCM_DEV_CONNECTION=scm:git@github.com:qiujuer/Genius-Android.git

POM_LICENCE_COMMENTS=A business-friendly OSS license
POM_LICENCE_NAME=The Apache Software License, Version 2.0
POM_LICENCE_URL=http://www.apache.org/licenses/LICENSE-2.0.txt
POM_LICENCE_DIST=repo

POM_DEVELOPER_ID=qiujuer
POM_DEVELOPER_NAME=Qiu Ju
POM_DEVELOPER_EMAIL=qiujuer@live.cn
POM_DEVELOPER_URL=http://www.qiujuer.net
POM_ISSUE_MANAGEMENT_SYSTEM=Github
POM_ISSUE_MANAGEMENT_URL=https://github.com/qiujuer/Genius-Android/issues
POM_INCEPTION_YEAR=2014
```

### 4. Create gradle.properties in each sub-project

See `gradle-lib.properties`
The values in this file are specific to the sub-project (and override those in the root `gradle.properties`). In this example, this is just the name, artifactId and packaging type:

```properties
POM_NAME=Genius Android Lib
POM_ARTIFACT_ID=genius
POM_PACKAGING=aar

VERSION_NAME=1.0.0-SNAPSHOT
VERSION_CODE=1
```

The `VERSION_NAME` value is important. If it contains the keyword `SNAPSHOT` then the build will upload to the snapshot server, if not then to the release server.


### 5. Call the script from each sub-modules build.gradle

Add the following at the end of each `build.gradle` that you wish to upload:

```groovy
android {
    sourceSets {
        main {
            manifest.srcFile 'src/main/AndroidManifest.xml'
            java.srcDirs = ['src/main/java']
            res.srcDirs = ['src/main/res']
        }
    }
    lintOptions {
        abortOnError false
    }
}
apply from: 'https://raw.github.com/qiujuer/BeFoot/master/blog/gradle-mvn-push/gradle-mvn-push.gradle'
```

### 6. Build and Push

You can now build and push:

```bash
$ gradle clean build uploadArchives
```
	
### Other Properties

There are other properties which can be set:

```
RELEASE_REPOSITORY_URL (defaults to Maven Central's staging server)
SNAPSHOT_REPOSITORY_URL (defaults to Maven Central's snapshot server)
```


## License

Copyright 2014-2015 Qiujuer.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.