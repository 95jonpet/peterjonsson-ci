import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

public class RpmbuildSpec extends JenkinsPipelineSpecification {
    def rpmbuild = null

	def setup() {
		rpmbuild = loadPipelineScriptForTest("vars/rpmbuild.groovy")
	}

    def "rpmbuild function creates working directories for rpmbuild"() {
        
    }
}
