import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.junit.Test

class RpmbuildSpec extends JenkinsPipelineSpecification {
    def rpmbuild = null

	def setup() {
		rpmbuild = loadPipelineScriptForTest('vars/rpmbuild.groovy')
	}

    @Test
    def '[rpmbuild] will run rpmbuild with a copied topdir under target/rpmbuild-tmp'() {
        when:
            rpmbuild version: '1.0.0', release: '1', topdir: topdir, specfile: specfile

        // TODO Handle absolute specfile -> resolve under target/rpmbuild-tmp by relativizing to topdir and appending to target/rpmbuild-tmp
        then:
            1 * getPipelineMock('sh')({
                it =~ 'mkdir -p target/rpmbuild-tmp/\\{BUILD,RPMS,SOURCES,SPECS,SRPMS\\}'
                it =~ /cp -r "$topdir" "target\/rpmbuild-tmp"/
                it =~ /rpmbuild -bb .* --define "_topdir target\/rpmbuild-tmp" .* $newSpecfile/
            })

        where:
            topdir                | specfile      | newSpecfile
            'target/rpmbuild-tmp' | 'first.spec'  | 'target/rpmbuild-tmp/SPECS/first.spec'
            '~/.rpmbuild'         | 'second.spec' | 'target/rpmbuild-tmp/SPECS/second.spec'
    }

    @Test
    def '[rpmbuild] will run rpmbuild -bb with version and release variables'() {
        when:
            rpmbuild version: version, release: release, topdir: topdir, specfile: 'test.spec'

        then:
			1 * getPipelineMock('sh')({
                it =~ /rpmbuild -bb .* --define "rel ${release}"/
                it =~ /rpmbuild -bb .* --define "ver ${version}"/
                it =~ 'rpmbuild -bb .* target/rpmbuild-tmp/SPECS'
            })

        where:
            version | release | topdir
            '1.0.0' | 1       | 'target/rpmbuild-tmp'
            '0.1'   | 6       | '~/.rpmbuild'
    }
}
