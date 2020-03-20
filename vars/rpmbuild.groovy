import static java.util.Objects.requireNonNull
import static se.peterjonsson.jenkins.SharedLibraryConstants.RPMBUILD_TARGET

def call(Map config, Closure body = {}) {
    String version = requireNonNull(config.version, 'version must not be null')
    String release = requireNonNull(config.release, 'release must not be null')
    String topdir = requireNonNull(config.topdir, 'topdir must not be null')
    String specfile = requireNonNull(config.specfile, 'specfile must not be null')

    sh """
        mkdir -p $RPMBUILD_TARGET/{BUILD,RPMS,SOURCES,SPECS,SRPMS}
        cp -r "$topdir/." "$RPMBUILD_TARGET"
    """

    body()

    sh """
        rpmbuild -bb \
            --define "_topdir \$(pwd)/$RPMBUILD_TARGET" \
            --define "ver $version" \
            --define "rel $release" \
            "$RPMBUILD_TARGET/SPECS/$specfile"
    """
}
