import static java.util.Objects.requireNonNull
import static se.peterjonsson.jenkins.SharedLibraryConstants.RPMBUILD_TARGET

def call(Map config) {
    String original = requireNonNull(config.original, 'original must not be null')
    String modified = requireNonNull(config.modified, 'modified must not be null')
    String output = requireNonNull(config.output, 'output must not be null')

    sh "diff -Naur \"$RPMBUILD_TARGET/$original\" \"$RPMBUILD_TARGET/$modified\" > \"$RPMBUILD_TARGET/$output\""
}
