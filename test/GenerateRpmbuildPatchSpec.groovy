import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification
import org.junit.Test

class GenerateRpmbuildPatchSpec extends JenkinsPipelineSpecification {
    def generateRpmbuildPatch = null

    def setup() {
        generateRpmbuildPatch = loadPipelineScriptForTest('vars/generateRpmbuildPatch.groovy')
    }

    @Test
    def '[generateRpmbuildPatch] will run [diff "$original" "$modified" > "$output"]'() {
        when:
            generateRpmbuildPatch(original: original, modified: modified, output: output)

        then:
            1 * getPipelineMock('sh')({ it =~ /diff .*".*$original" ".*$modified" > ".*$output"/ })

        where:
            original               | modified          | output
            'something-1.2.3-orig' | 'something-1.2.3' | 'mychanges.patch'
            'logback.orig.xml'     | 'logback.xml'     | 'logback.patch'
    }
}
