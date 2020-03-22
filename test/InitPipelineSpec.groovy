import java.util.Properties
import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.apache.maven.model.Model
import org.junit.Test

class InitPipelineSpec extends JenkinsPipelineSpecification {
    def initPipeline = null
    def currentBuild = [:]
    def env = [:]

	def setup() {
		initPipeline = loadPipelineScriptForTest('vars/initPipeline.groovy')
        initPipeline.getBinding().setVariable('currentBuild', currentBuild)
        initPipeline.getBinding().setVariable('env', env)
	}

    @Test
    def '[initPipeline] will set [env.PJCI_MAIN_BRANCH] to mainBranch argument'() {
        when:
            initPipeline([mainBranch: branch])

        then:
            assert env.PJCI_MAIN_BRANCH == branch

        where:
            branch << ['main', 'prod', 'release']
    }

    @Test
    def '[initPipeline] will set [env.PJCI_MAIN_BRANCH] to "master" if no mainBranch argument is given'() {
        when:
            initPipeline()

        then:
            assert env.PJCI_MAIN_BRANCH == 'master'
    }

    @Test
    def '[initPipeline] will run [initMavenPipeline] if a pom.xml file exists'() {
        given:
            getPipelineMock('fileExists')('pom.xml') >> true

        when:
            initPipeline()

        then:
            1 * getPipelineMock('initMavenPipeline.call')()
    }

    @Test
    def '[initPipeline] will not run [initMavenPipeline] if no pom.xml file exists'() {
        given:
            getPipelineMock('fileExists')('pom.xml') >> false

        when:
            initPipeline()

        then:
            0 * getPipelineMock('initMavenPipeline.call')()
    }
}
