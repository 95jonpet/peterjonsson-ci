def call(Map config = [:]) {
    String mainBranch = config.mainBranch ?: 'master'
    String phases = config.phases ?: 'clean verify'

    def parameters = [
        '--batch-mode',
        '--errors',
        '--fail-at-end',
        '--show-version',
    ]

    // Remove SNAPSHOT from version when building the main branch.
    if (env.BRANCH_NAME == mainBranch) {
        parameters.add('-Dchangelist=')
    }

    try {
        sh "mvn ${parameters.join(' ')} $phases"
    } catch (Exception e) {
        throw e
    } finally {
        junit '**/target/**/*.xml'
    }
}
