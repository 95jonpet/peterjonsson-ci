import static java.util.Objects.requireNonNull

def call(Map config, Closure body = {}) {
    String version = requireNonNull(config.version, 'version must not be null')
    String release = requireNonNull(config.release, 'release must not be null')
    String topdir = requireNonNull(config.topdir, 'topdir must not be null')
    String specfile = requireNonNull(config.specfile, 'specfile must not be null')

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
            "target/rpmbuild-tmp/SPECS/$specfile"
    """
}
