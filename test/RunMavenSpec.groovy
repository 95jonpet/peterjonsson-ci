import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.junit.Test

class RunMavenSpec extends JenkinsPipelineSpecification {
    def runMaven = null

	def setup() {
		runMaven = loadPipelineScriptForTest('vars/runMaven.groovy')
	}

    @Test
    def '[runMaven] will run mvn with phases clean and verify by default'() {
        when:
            runMaven()

        then:
            1 * getPipelineMock('sh')({ it =~ /mvn .* clean verify/ })
    }

    @Test
    def '[runMaven] will run mvn with specified phases'() {
        when:
            runMaven([phases: phases])

        then:
            1 * getPipelineMock('sh')({ it =~ /mvn .* $phases/ })

        where:
            phases << ['clean', 'clean test', 'clean verify deploy']
    }

    @Test
    def '[runMaven] will pass [-Dchangelist=] to mvn command when on the main branch'() {
        given:
            runMaven.getBinding().setVariable('env', [
                BRANCH_NAME: 'prod'
            ])

        when:
            runMaven([mainBranch: 'prod'])

        then:
            1 * getPipelineMock('sh')({ it =~ /mvn .* -Dchangelist= / })
    }

    @Test
    def '[runMaven] not pass [-Dchangelist=] to mvn command when not on the main branch'() {
        given:
            runMaven.getBinding().setVariable('env', [
                BRANCH_NAME: 'dev'
            ])

        when:
            runMaven([mainBranch: 'prod'])

        then:
            0 * getPipelineMock('sh')({ it =~ /mvn .* -Dchangelist= / })
    }

    @Test
    def '[runMaven] will run junit'() {
        when:
            runMaven()

        then:
            1 * getPipelineMock('junit')('**/target/**/*.xml')
    }

    @Test
    def '[runMaven] will run junit when the maven build fails'() {
        given:
            getPipelineMock('sh')(_) >> { throw new Exception() }

        when:
            try {
                runMaven()
            } catch (Exception e) {}

        then:
            1 * getPipelineMock('junit')('**/target/**/*.xml')
    }
}
