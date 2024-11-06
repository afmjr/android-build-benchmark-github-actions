import org.gradle.api.Plugin
import org.gradle.api.Project
import com.google.gson.Gson
import java.io.File

class FetchModulesWithTestsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task('fetchModulesWithTests') {
            doLast {
                def modulesWithTests = []
                project.rootProject.subprojects.each { subproject ->
                    def hasTests = subproject.fileTree('src/test').files.any() || subproject.fileTree('src/androidTest').files.any()
                    if (hasTests) {
                        modulesWithTests << subproject.name
                    }
                }
                def json = new Gson().toJson(modulesWithTests)
                def outputFile = new File(project.rootProject.buildDir, 'modulesWithTests.json')
                outputFile.parentFile.mkdirs()
                outputFile.text = json
                println "Modules with tests: ${modulesWithTests}"
                println "Saved to: ${outputFile.absolutePath}"
            }
        }
    }
}
