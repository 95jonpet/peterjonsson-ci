def call(Map config = [:]) {
    env.PJCI_MAIN_BRANCH = config.mainBranch ?: 'master'

    if (fileExists('pom.xml')) {
        initMavenPipeline()
    }
}
