import java.util.Properties

def call(Map config = [:]) {
    String mainBranch = config.mainBranch ?: 'master'

    def pom = readMavenPom()
    String versionTemplate = pom.getVersion()
    Properties properties = pom.getProperties()

    String revision = properties.getProperty('revision', '')
    String changelist = properties.getProperty('changelist', '-SNAPSHOT')
    String sha1 = properties.getProperty('sha1', env.BRANCH_NAME != mainBranch ? "${env.BRANCH_NAME}-${env.BUILD_NUMBER}" : (String) env.BUILD_NUMBER)

    String version = versionTemplate
        .replaceAll('\\$\\{revision\\}', revision)
        .replaceAll('\\$\\{changelist\\}', env.BRANCH_NAME != mainBranch ? changelist : '')
        .replaceAll('\\$\\{sha1\\}', "-${sha1}")

    currentBuild.displayName = version
}
