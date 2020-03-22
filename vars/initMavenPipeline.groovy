import static java.util.Objects.requireNonNull
import java.util.Properties

def call(Map config = [:]) {
    String mainBranch = requireNonNull(env.PJCI_MAIN_BRANCH, 'env.PJCI_MAIN_BRANCH must not be null')

    def pom = readMavenPom()
    String versionTemplate = pom.getVersion()
    Properties properties = pom.getProperties()

    String revision = properties.getProperty('revision', '')
    String changelist = env.BRANCH_NAME != mainBranch ? properties.getProperty('changelist', '-SNAPSHOT') : ''
    String sha1 = '-' + properties.getProperty('sha1', env.BRANCH_NAME != mainBranch ? "${env.BRANCH_NAME}.${env.BUILD_NUMBER}" : (String) env.BUILD_NUMBER)

    String version = versionTemplate
        .replaceAll('\\$\\{revision\\}', revision)
        .replaceAll('\\$\\{changelist\\}', changelist)
        .replaceAll('\\$\\{sha1\\}', sha1)

    // Set Maven related pipeline variables.
    env.PJCI_MAVEN_REVISION = revision
    env.PJCI_MAVEN_CHANGELIST = changelist
    env.PJCI_MAVEN_SHA1 = sha1
    env.PJCI_MAVEN_VERSION = version

    currentBuild.displayName = version
}
