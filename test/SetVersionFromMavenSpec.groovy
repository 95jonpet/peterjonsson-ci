import java.util.Properties
import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.apache.maven.model.Model
import org.junit.Test

class SetVersionFromMavenSpec extends JenkinsPipelineSpecification {
    def setVersionFromMaven = null
    def currentBuild = [:]

	def setup() {
		setVersionFromMaven = loadPipelineScriptForTest('vars/setVersionFromMaven.groovy')
        setVersionFromMaven.getBinding().setVariable('currentBuild', currentBuild)
	}

    @Test
    def '[setVersionFromMaven] will set currentBuild.displayName correctly'() {
        given:
            setVersionFromMaven.getBinding().setVariable('env', [
                BRANCH_NAME: branch,
                BUILD_NUMBER: buildNumber
            ])
            Properties properties = new Properties()
            properties.setProperty('revision', revision)
            properties.setProperty('changelist', changelist)

            Model pomModel = new Model()
            pomModel.setVersion(versionTemplate)
            pomModel.setProperties(properties)
            getPipelineMock('readMavenPom')() >> pomModel

        when:
            setVersionFromMaven()

        then:
            assert currentBuild.displayName == expectedVersion

        where:
            versionTemplate                   | revision | changelist  | buildNumber | branch   | expectedVersion
            '${revision}${sha1}${changelist}' | '0.1.0'  | '-SNAPSHOT' | 1           | 'master' | '0.1.0-1'
            '${revision}${sha1}${changelist}' | '0.1.0'  | '-SNAPSHOT' | 1           | 'dev'    | '0.1.0-dev-1-SNAPSHOT'
            '${revision}${sha1}${changelist}' | '1.0.4'  | '-SNAPSHOT' | 47          | 'master' | '1.0.4-47'
            '${revision}${sha1}${changelist}' | '1.0.4'  | '-SNAPSHOT' | 47          | 'dev'    | '1.0.4-dev-47-SNAPSHOT'
            '${revision}${changelist}'        | '7.4.3'  | '-SNAPSHOT' | 1           | 'master' | '7.4.3'
            '${revision}${changelist}'        | '7.4.3'  | '-SNAPSHOT' | 1           | 'dev'    | '7.4.3-SNAPSHOT'
            '${revision}${changelist}'        | '0.4'    | '-SNAPSHOT' | 47          | 'master' | '0.4'
            '${revision}${changelist}'        | '0.4'    | '-SNAPSHOT' | 47          | 'dev'    | '0.4-SNAPSHOT'
    }
}
