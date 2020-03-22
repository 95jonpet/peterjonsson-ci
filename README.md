# peterjonsson-ci

[![Build Status](https://jenkins.peterjonsson.se/buildStatus/icon?job=peterjonsson-ci%2Fmaster)](https://jenkins.peterjonsson.se/job/peterjonsson-ci/job/master/)

This is a small Jenkins shared library for CI/CD.

Author: [Peter Jonsson](https://peterjonsson.se)

---

## Usage

Important: The `initPipeline()` step should always be used before any other steps from this shared library.

#### initPipeline

Initializes the shared library. This step should always be called first.

| Parameter    | Default Value | Description                          |
| :----------- | :------------ | :----------------------------------- |
| `mainBranch` | `master`      | Main branch representing production. |

This step will call `initMavenPipeline()` if there is a `pom.xml` file in the root directory of the current workspace.

```groovy
initPipeline()

initPipeline([mainBranch: 'prod'])
```

### Maven

#### initMavenPipeline

Sets the following environment (`env`) variables for later use with Maven:

| Environment Variable    | Maven Property | Description                                                      |
| :---------------------- | :------------- | :--------------------------------------------------------------- |
| `PJCI_MAVEN_REVISION`   | `revision`     | Current revision (i.e. `1.0.0`) as read from the `pom.xml` file. |
| `PJCI_MAVEN_CHANGELIST` | `changelist`   | `-SNAPSHOT` when not on main branch, otherwise empty.            |
| `PJCI_MAVEN_SHA1`       | `sha1`         | Unique build identifier (i.e. `-BRANCH_NAME.BUILD_NUMBER`).      |
| `PJCI_MAVEN_VERSION`    | `version`      | Interpolated Maven version (i.e. `1.0.0-featureX.7-SNAPSHOT`).   |

This step should typically not be called manually.

#### runMaven

Run Maven. This requires `mvn` to be present on the current agent.

| Parameter | Default Value  | Description                                  |
| :-------- | :------------- | :------------------------------------------- |
| `phases`  | `clean verify` | Space separated list of Maven phases to run. |

Example usage:

```groovy
runMaven()

runMaven([phases: 'clean test'])
```

### RPM Steps

#### generateRpmbuildPatch

Generates a source code patch, that `rpmbuild` can use, from a modified file and its original version.

| Parameter  | Description                                                                  |
| :--------- | :--------------------------------------------------------------------------- |
| `original` | Path relative to the `rpmbuild topdir` for the unaltered file.               |
| `modified` | Path relative to the `rpmbuild topdir` for the modified file.                |
| `output`   | Path relative to the `rpmbuild topdir` for where the patch should be placed. |

Example usage with the `rpmbuild` step:

```groovy
rpmbuild(version: '1.0.0', release: '1', topdir: 'src/rpm', specfile: 'hello-world.spec') {
    generateRpmbuildPatch([
        original: 'SOURCES/logback.orig.xml', // Relative to src/rpm.
        modified: 'SOURCES/logback.xml', // Relative to src/rpm.
        output: 'SOURCES/logback.patch' // Relative to src/rpm.
    ])
}
```

#### rpmbuild

Build an RPM package from a `.spec` file. This requires `rpmbuild` to be present on the current agent.

| Parameter  | Required | Description                                                       |
| :--------- | :------- | :---------------------------------------------------------------- |
| `version`  | Yes      | RPM version (i.e. `1.0.0`).                                       |
| `release`  | Yes      | RPM release (i.e. `1`).                                           |
| `topdir`   | Yes      | Rpmbuild top directory (it contains the `SPECS` folder).          |
| `specfile` | Yes      | Name of the `.spec` file to build from the `topdir/SPECS` folder. |
| Closure    | No       | Optional closure to execute before building the RPM.              |

```groovy
rpmbuild(version: '0.1', release: '8', topdir: 'src/rpm', specfile: 'my-rpm.spec')

rpmbuild(version: '1.0.0', release: '1', topdir: 'src/rpm', specfile: 'hello-world.spec') {
    echo "Called after preparing rpmbuild but before execution."
}
```
