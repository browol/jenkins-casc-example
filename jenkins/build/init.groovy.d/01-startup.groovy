import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import groovy.io.FileType
import java.text.SimpleDateFormat

env = System.getenv()
String jenkinsHome = env['JENKINS_HOME']
File workspace = new File(jenkinsHome, 'workspace')
JenkinsJobManagement jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
DslScriptLoader dslScriptLoader = new DslScriptLoader(jobManagement)

def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

ArrayList<File> files = []
new File(jenkinsHome, 'init.script.dsl').eachFileRecurse(FileType.FILES) { file ->
    files << file
}
files.sort{ it.name }.each { File file ->
    println("${sdf.format(new Date())} load: ${file.name}")
    dslScriptLoader.runScript(file.text)
    println("${sdf.format(new Date())} loaded: ${file.name}")
}
