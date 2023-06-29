def env = System.getenv()

pipelineJob('Nginx Deployment') {
    displayName('Nginx Deployment')
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(env['CONFIGURATION_REPOSITORY_URL'])
                        credentials('git-token-credentials')
                    }
                    branch(env['CONFIGURATION_REPOSITORY_BRANCH'])
                }
                scriptPath('jenkins/jobs/pipelines/jenkinsfile.nginx-deployment')
            }
        }
    }
    triggers {
        upstream('Docker Image Builder', 'SUCCESS')
    }
}
