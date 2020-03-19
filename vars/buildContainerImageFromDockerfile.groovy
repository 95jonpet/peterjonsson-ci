def call(Map config) {
    // config.imageName
    // config.imageTag
    // config.imagePath

    def image = docker.build(config.imageName, config.imagePath)
}
