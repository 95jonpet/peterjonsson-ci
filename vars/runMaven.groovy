import static java.util.Objects.requireNonNull

def call(Map config = [:]) {
    String phases = config.phases ?: 'clean verify'
    String mavenRevision = requireNonNull(env.PJCI_MAVEN_REVISION, 'env.PJCI_MAVEN_REVISION must not be null')
    String mavenChangelist = requireNonNull(env.PJCI_MAVEN_CHANGELIST, 'env.PJCI_MAVEN_CHANGELIST must not be null')
    String mavenSha1 = requireNonNull(env.PJCI_MAVEN_SHA1, 'env.PJCI_MAVEN_SHA1 must not be null')

    def parameters = [
        '--batch-mode',
        '--errors',
        '--fail-at-end',
        '--show-version',
    ]

    // Add parameters set in initMavenPipeline().
    parameters.add("-Drevision=$mavenRevision")
    parameters.add("-Dchangelist=$mavenChangelist")
    parameters.add("-Dsha1=$mavenSha1")

    try {
        sh "mvn ${parameters.join(' ')} $phases"
    } catch (Exception e) {
        throw e
    } finally {
        junit '**/target/**/*.xml'
    }
}
