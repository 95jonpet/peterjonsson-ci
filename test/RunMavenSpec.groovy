import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.junit.Test

class RunMavenSpec extends JenkinsPipelineSpecification {
    def runMaven = null
    def env = [
        PJCI_MAVEN_REVISION: '1.0.0',
        PJCI_MAVEN_CHANGELIST: '-SNAPSHOT',
        PJCI_MAVEN_SHA1: ''
    ]

	def setup() {
		runMaven = loadPipelineScriptForTest('vars/runMaven.groovy')
        runMaven.getBinding().setVariable('env', env)
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
    def '[runMaven] will pass [-Drevision=env.PJCI_MAVEN_REVISION] to mvn command'() {
        given:
            runMaven.getBinding().setVariable('env', [
                *:env,
                PJCI_MAVEN_REVISION: revision
            ])

        when:
            runMaven()

        then:
            1 * getPipelineMock('sh')({ it =~ /mvn .* -Drevision=$revision / })

        where:
            revision << ['1.0.0', '0.3', '7.3.8']
    }

    @Test
    def '[runMaven] will pass [-Dsha1=env.PJCI_MAVEN_SHA1] to mvn command'() {
        given:
            runMaven.getBinding().setVariable('env', [
                *:env,
                PJCI_MAVEN_SHA1: sha1
            ])

        when:
            runMaven()

        then:
            1 * getPipelineMock('sh')({ it =~ /mvn .* -Dsha1=$sha1 / })

        where:
            sha1 << ['', '-featureX-1', '-1337']
    }

    @Test
    def '[runMaven] will pass [-Dchangelist=env.PJCI_MAVEN_CHANGELIST] to mvn command'() {
        given:
            runMaven.getBinding().setVariable('env', [
                *:env,
                PJCI_MAVEN_CHANGELIST: changelist
            ])

        when:
            runMaven()

        then:
            1 * getPipelineMock('sh')({ it =~ /mvn .* -Dchangelist=$changelist / })

        where:
            changelist << ['-SNAPSHOT', '']
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
