import java.util.Objects

def call(Map config, Closure body = {}) {
    String version = Objects.requireNonNull(config.version)
    String release = Objects.requireNonNull(config.release)
    String topdir = Objects.requireNonNull(config.topdir)
    String specfile = Objects.requireNonNull(config.specfile)

    String specfilePath = "target/rpmbuild-tmp/SPECS/${specfile}"

    sh """
        mkdir -p target/rpmbuild-tmp/{BUILD,RPMS,SOURCES,SPECS,SRPMS}
        cp -r "$topdir" "target/rpmbuild-tmp"
    """

    body()

    sh """
        rpmbuild -bb \
            --define "_topdir target/rpmbuild-tmp" \
            --define "ver $version" \
            --define "rel $release" \
            $specfilePath
    """
}
